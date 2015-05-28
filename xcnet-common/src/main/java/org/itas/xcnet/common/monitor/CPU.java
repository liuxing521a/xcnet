/*
 * Copyright 2014-2015 itas group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.itas.xcnet.common.monitor;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月22日下午4:49:13
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.StringTokenizer;
 
public class CPU {
    private static final int CPUTIME = 30;
 
    private static final int PERCENT = 100;
 
    private static final int FAULTLENGTH = 10;
 
    private static String linuxVersion = null;
 
    public double getCpuRatio() {
        // 操作系统
        String osName = System.getProperty("os.name");
        double cpuRatio = 0;
        if (osName.toLowerCase().startsWith("windows")) {
            cpuRatio = this.getCpuRatioForWindows();
 
        } else {
            cpuRatio = this.getCpuRateForLinux();
        }
        return cpuRatio;
    }
 
    /**
     * 获得当前的监控对象.
     * 
     * @return 返回构造好的监控对象
     */
    public MonitorInfoBean getMonitorInfoBean() throws Exception {
 
        // 操作系统
        String osName = System.getProperty("os.name");
 
        // 获得线程总数
        ThreadGroup parentThread;
        for (parentThread = Thread.currentThread().getThreadGroup(); parentThread
                .getParent() != null; parentThread = parentThread.getParent())
            ;
        int totalThread = parentThread.activeCount();
 
        double cpuRatio = 0;
        if (osName.toLowerCase().startsWith("windows")) {
            cpuRatio = this.getCpuRatioForWindows();
        } else {
            cpuRatio = getCpuRateForLinux();
        }
 
        // 构造返回对象
        MonitorInfoBean infoBean = new MonitorInfoBean();
        infoBean.setTotalThread(totalThread);
        infoBean.setCpuRatio(cpuRatio);
        return infoBean;
    }
 
    private static double getCpuRateForLinux() {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader brStat = null;
        StringTokenizer tokenStat = null;
        try {
            System.out.println("Get usage rate of CUP , linux version: "
                    + linuxVersion);
 
            Process process = Runtime.getRuntime().exec("top -b -n 1");
            is = process.getInputStream();
            isr = new InputStreamReader(is);
            brStat = new BufferedReader(isr);
 
            if (linuxVersion.equals("2.4")) {
                brStat.readLine();
                brStat.readLine();
                brStat.readLine();
                brStat.readLine();
 
                tokenStat = new StringTokenizer(brStat.readLine());
                tokenStat.nextToken();
                tokenStat.nextToken();
                String user = tokenStat.nextToken();
                tokenStat.nextToken();
                String system = tokenStat.nextToken();
                tokenStat.nextToken();
                String nice = tokenStat.nextToken();
 
                System.out.println(user + " , " + system + " , " + nice);
 
                user = user.substring(0, user.indexOf("%"));
                system = system.substring(0, system.indexOf("%"));
                nice = nice.substring(0, nice.indexOf("%"));
 
                float userUsage = new Float(user).floatValue();
                float systemUsage = new Float(system).floatValue();
                float niceUsage = new Float(nice).floatValue();
 
                return (userUsage + systemUsage + niceUsage) / 100;
            } else {
                brStat.readLine();
                brStat.readLine();
 
                tokenStat = new StringTokenizer(brStat.readLine());
                tokenStat.nextToken();
                tokenStat.nextToken();
                tokenStat.nextToken();
                tokenStat.nextToken();
                tokenStat.nextToken();
                tokenStat.nextToken();
                tokenStat.nextToken();
                String cpuUsage = tokenStat.nextToken();
 
                System.out.println("CPU idle : " + cpuUsage);
                Float usage = new Float(cpuUsage.substring(0, cpuUsage
                        .indexOf("%")));
 
                return (1 - usage.floatValue() / 100);
            }
 
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            freeResource(is, isr, brStat);
            return 1;
        } finally {
            freeResource(is, isr, brStat);
        }
 
    }
 
    private static void freeResource(InputStream is, InputStreamReader isr,
            BufferedReader br) {
        try {
            if (is != null)
                is.close();
            if (isr != null)
                isr.close();
            if (br != null)
                br.close();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }
 
    /**
     * 获得CPU使用率.
     * 
     * @return 返回cpu使用率
     */
    private double getCpuRatioForWindows() {
        try {
            String procCmd = System.getenv("windir")
                    + "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,"
                    + "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
            // 取进程信息
            long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
            Thread.sleep(CPUTIME);
            long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
            if (c0 != null && c1 != null) {
                long idletime = c1[0] - c0[0];
                long busytime = c1[1] - c0[1];
                return Double.valueOf(
                        PERCENT * (busytime) / (busytime + idletime))
                        .doubleValue();
            } else {
                return 0.0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }
 
    /**
     * 
     * 读取CPU信息.
     * 
     * @param proc
     */
    private long[] readCpu(final Process proc) {
        long[] retn = new long[2];
        try {
            proc.getOutputStream().close();
            InputStreamReader ir = new InputStreamReader(proc.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line = input.readLine();
            if (line == null || line.length() < FAULTLENGTH) {
                return null;
            }
            int capidx = line.indexOf("Caption");
            int cmdidx = line.indexOf("CommandLine");
            int rocidx = line.indexOf("ReadOperationCount");
            int umtidx = line.indexOf("UserModeTime");
            int kmtidx = line.indexOf("KernelModeTime");
            int wocidx = line.indexOf("WriteOperationCount");
            long idletime = 0;
            long kneltime = 0;
            long usertime = 0;
            while ((line = input.readLine()) != null) {
                if (line.length() < wocidx) {
                    continue;
                }
                String caption = substring(line, capidx, cmdidx - 1)
                        .trim();
                String cmd = substring(line, cmdidx, kmtidx - 1).trim();
                if (cmd.indexOf("wmic.exe") >= 0) {
                    continue;
                }
                if (caption.equals("System Idle Process")
                        || caption.equals("System")) {
                    idletime += Long.valueOf(
                            substring(line, kmtidx, rocidx - 1).trim())
                            .longValue();
                    idletime += Long.valueOf(
                            substring(line, umtidx, wocidx - 1).trim())
                            .longValue();
                    continue;
                }
 
                kneltime += Long.valueOf(
                        substring(line, kmtidx, rocidx - 1).trim())
                        .longValue();
                usertime += Long.valueOf(
                        substring(line, umtidx, wocidx - 1).trim())
                        .longValue();
            }
            retn[0] = idletime;
            retn[1] = kneltime + usertime;
            return retn;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                proc.getInputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    /** *//** 
     * 由于String.subString对汉字处理存在问题（把一个汉字视为一个字节)，因此在 
     * 包含汉字的字符串时存在隐患，现调整如下： 
     * @param src 要截取的字符串 
     * @param start_idx 开始坐标（包括该坐标) 
     * @param end_idx   截止坐标（包括该坐标） 
     * @return 
     */  
    public static String substring(String src, int start_idx, int end_idx){  
        byte[] b = src.getBytes();  
        String tgt = "";  
        for(int i=start_idx; i<=end_idx; i++){  
            tgt +=(char)b[i];  
        }  
        return tgt;  
    } 
 
    public static void main(String[] args) throws Exception {
        CPU c = new CPU();
        System.out.println("cpu占有率1=" + c.getCpuRatio());
        System.out.println("cpu占有率2=" + c.getCpuRatioForWindows());
    }
}
