import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.List;

import org.nlogo.api.DefaultClassManager;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.PrimitiveManager;

/**
 * 
 */

/**
 * @author gary
 *
 */
public class JVMGRExtension extends DefaultClassManager {
	static final String EXTENSION_NAME = "mgr";
	private static MemoryMXBean mem = null;
	private static RuntimeMXBean rt = null;
	private static ThreadMXBean thr = null;
	private static List<GarbageCollectorMXBean> gc = null;
	private static OperatingSystemMXBean os = null;

	@Override
	public void load(PrimitiveManager primManager) throws ExtensionException {
		primManager.addPrimitive("mem", new MemoryUsage(MemoryUsage.UsageCommand.USED_BYTES));
		primManager.addPrimitive("mem-p", new MemoryUsage(MemoryUsage.UsageCommand.USED_PROP));
		primManager.addPrimitive("mem-pct-str", new MemoryUsage(MemoryUsage.UsageCommand.USED_PCT_STR));
		primManager.addPrimitive("heap-used", new MemoryUsage(MemoryUsage.UsageCommand.USED_HEAP));
		primManager.addPrimitive("non-heap-used", new MemoryUsage(MemoryUsage.UsageCommand.USED_NON_HEAP));
		primManager.addPrimitive("heap-alloc", new MemoryUsage(MemoryUsage.UsageCommand.COMMITTED_HEAP));
		primManager.addPrimitive("non-heap-alloc", new MemoryUsage(MemoryUsage.UsageCommand.COMMITTED_NON_HEAP));
		primManager.addPrimitive("heap-init", new MemoryUsage(MemoryUsage.UsageCommand.INIT_HEAP));
		primManager.addPrimitive("non-heap-init", new MemoryUsage(MemoryUsage.UsageCommand.INIT_NON_HEAP));
		primManager.addPrimitive("heap-max", new MemoryUsage(MemoryUsage.UsageCommand.MAX_HEAP));
		primManager.addPrimitive("non-heap-max", new MemoryUsage(MemoryUsage.UsageCommand.MAX_NON_HEAP));
		primManager.addPrimitive("mem-str", new MemoryUsage(MemoryUsage.UsageCommand.USED_HR));
		primManager.addPrimitive("heap-used-str", new MemoryUsage(MemoryUsage.UsageCommand.USED_HEAP_HR));
		primManager.addPrimitive("non-heap-used-str", new MemoryUsage(MemoryUsage.UsageCommand.USED_NON_HEAP_HR));
		primManager.addPrimitive("alloc-str", new MemoryUsage(MemoryUsage.UsageCommand.COMMITTED_HR));
		primManager.addPrimitive("threads", new ProcessInfo(ProcessInfo.InfoCommand.THREADS));
		primManager.addPrimitive("peak-threads", new ProcessInfo(ProcessInfo.InfoCommand.PEAK_THREADS));
		primManager.addPrimitive("all-threads", new ProcessInfo(ProcessInfo.InfoCommand.ALL_THREADS));
		primManager.addPrimitive("thread-cpu", new ProcessInfo(ProcessInfo.InfoCommand.THREAD_CPU));
		primManager.addPrimitive("thread-user", new ProcessInfo(ProcessInfo.InfoCommand.THREAD_USER));
		primManager.addPrimitive("thread-system", new ProcessInfo(ProcessInfo.InfoCommand.THREAD_SYSTEM));
		primManager.addPrimitive("cpu-time", new ProcessInfo(ProcessInfo.InfoCommand.CPU_TIME));
		primManager.addPrimitive("user-time", new ProcessInfo(ProcessInfo.InfoCommand.USER_TIME));
		primManager.addPrimitive("system-time", new ProcessInfo(ProcessInfo.InfoCommand.SYSTEM_TIME));
		primManager.addPrimitive("blocked-time", new ProcessInfo(ProcessInfo.InfoCommand.BLOCKED_TIME));
		primManager.addPrimitive("blocked-count", new ProcessInfo(ProcessInfo.InfoCommand.BLOCKED_COUNT));
		primManager.addPrimitive("waited-time", new ProcessInfo(ProcessInfo.InfoCommand.WAITED_TIME));
		primManager.addPrimitive("waited-count", new ProcessInfo(ProcessInfo.InfoCommand.WAITED_COUNT));
		primManager.addPrimitive("jvm-start-time", new ProcessInfo(ProcessInfo.InfoCommand.JVM_START_TIME));
		primManager.addPrimitive("jvm-uptime", new ProcessInfo(ProcessInfo.InfoCommand.JVM_UPTIME));
		primManager.addPrimitive("gc-count", new ProcessInfo(ProcessInfo.InfoCommand.GC_COUNT));
		primManager.addPrimitive("gc-time", new ProcessInfo(ProcessInfo.InfoCommand.GC_TIME));
		primManager.addPrimitive("jvm-proc-count", new ProcessInfo(ProcessInfo.InfoCommand.JVM_PROC_COUNT));
		primManager.addPrimitive("system-load", new ProcessInfo(ProcessInfo.InfoCommand.SYSTEM_LOAD));
		primManager.addPrimitive("jvm-name", new ProcessInfo(ProcessInfo.InfoCommand.JVM_NAME));
		primManager.addPrimitive("cpu-time-str", new ProcessInfo(ProcessInfo.InfoCommand.CPU_TIME_STR));
		primManager.addPrimitive("user-time-str", new ProcessInfo(ProcessInfo.InfoCommand.USER_TIME_STR));
		primManager.addPrimitive("system-time-str", new ProcessInfo(ProcessInfo.InfoCommand.SYSTEM_TIME_STR));
		primManager.addPrimitive("waited-time-str", new ProcessInfo(ProcessInfo.InfoCommand.WAITED_TIME_STR));
		primManager.addPrimitive("blocked-time-str", new ProcessInfo(ProcessInfo.InfoCommand.BLOCKED_TIME_STR));
		primManager.addPrimitive("jvm-uptime-str", new ProcessInfo(ProcessInfo.InfoCommand.JVM_UPTIME_STR));

		if(mem == null) {
			mem = ManagementFactory.getMemoryMXBean();
		}
		if(rt == null) {
			rt = ManagementFactory.getRuntimeMXBean();
		}
		if(thr == null) {
			thr = ManagementFactory.getThreadMXBean();
		}
		if(gc == null) {
			gc = ManagementFactory.getGarbageCollectorMXBeans();
		}
		if(os == null) {
			os = ManagementFactory.getOperatingSystemMXBean();
		}
	}

	/**
	 * @return initial heap memory requested from OS by the JVM -- may be undefined (-1)
	 */
	static long getHeapInitial() {
		if(mem != null) {
			return mem.getHeapMemoryUsage().getInit();
		}
		else {
			return -1L;
		}
	}
	
	/**
	 * @return heap memory guaranteed available to be used by the JVM
	 */
	static long getHeapCommitted() {
		if(mem != null) {
			return mem.getHeapMemoryUsage().getCommitted();
		}
		else {
			return -1L;
		}
	}
	
	/**
	 * @return maximum heap memory -- may be undefined (-1)
	 */
	static long getHeapMax() {
		if(mem != null) {
			return mem.getHeapMemoryUsage().getMax();
		}
		else {
			return -1L;
		}
	}
	
	/**
	 * @return heap memory used
	 */
	static long getHeapUsed() {
		if(mem != null) {
			return mem.getHeapMemoryUsage().getUsed();
		}
		else {
			return -1L;
		}
	}
	
	static long getNonHeapInitial() {
		if(mem != null) {
			return mem.getNonHeapMemoryUsage().getInit();
		}
		else {
			return -1L;
		}
	}
	
	static long getNonHeapCommitted() {
		if(mem != null) {
			return mem.getNonHeapMemoryUsage().getCommitted();
		}
		else {
			return -1L;
		}
	}
	
	static long getNonHeapMax() {
		if(mem != null) {
			return mem.getNonHeapMemoryUsage().getMax();
		}
		else {
			return -1L;
		}
	}
	
	static long getNonHeapUsed() {
		if(mem != null) {
			return mem.getNonHeapMemoryUsage().getUsed();
		}
		else {
			return -1L;
		}
	}

	/**
	 * @return an 'arbitrary' name representing the running JVM that could contain OS info
	 */
	static String getJVMName() {
		if(rt != null) {
			return rt.getName();
		}
		else {
			return "NA";
		}
	}
	
	static long getJVMUptimeMillis() {
		if(rt != null) {
			return rt.getUptime();
		}
		else {
			return -1L;
		}
	}
	
	static long getJVMStartTimeMillis() {
		if(rt != null) {
			return rt.getStartTime();
		}
		else {
			return -1L;
		}
	}
	
	/**
	 * @return the current number of live threads (daemon and non-daemon)
	 */
	static int getLiveThreadCount() {
		if(thr != null) {
			return thr.getThreadCount();
		}
		else {
			return -1;
		}
	}
	
	/**
	 * @return the highest number of live threads since JVM start (or peak reset)
	 */
	static int getPeakThreadCount() {
		if(thr != null) {
			return thr.getPeakThreadCount();
		}
		else {
			return -1;
		}
	}
	
	/**
	 * @return the number of threads created and also started since JVM start
	 */
	static long getTotalStartedThreadCount() {
		if(thr != null) {
			return thr.getTotalStartedThreadCount();
		}
		else {
			return -1L;
		}
	}
	
	/**
	 * @return the (approximate) number of nanoseconds this thread has been running on the
	 * CPU in system or user time, or -1 if the operation is not supported
	 */
	static long getCurrentThreadCpuTimeNanos() {
		if(thr != null && thr.isCurrentThreadCpuTimeSupported()) {
			return thr.getCurrentThreadCpuTime();
		}
		else {
			return -1L;
		}
	}
	
	/**
	 * @return the (approximate) number of nanoseconds this thread has been running on the
	 * CPU in user time, or -1 if the operation is not supported
	 */
	static long getCurrentThreadUserTimeNanos() {
		if(thr != null && thr.isCurrentThreadCpuTimeSupported()) {
			return thr.getCurrentThreadUserTime();
		}
		else {
			return -1L;
		}
	}
	
	static long getAllThreadCpuTimeNanos() {
		if(thr != null && thr.isThreadCpuTimeSupported()) {
			try {
				long[] thrs = thr.getAllThreadIds();
				
				long tot = 0L;
				
				for(int i = 0; i < thrs.length; i++) {
					long t = thr.getThreadCpuTime(thrs[i]);
					
					if(t >= 0L) {
						tot += t;
					}
				}
				
				return tot == 0L ? -1L : tot;
			} catch(SecurityException e) {
				return -1L;
			}
		} else {
			return -1L;
		}
	}
	
	static long getAllThreadUserTimeNanos() {
		if(thr != null && thr.isThreadCpuTimeSupported()) {
			try {
				long[] thrs = thr.getAllThreadIds();
				
				long tot = 0L;
				
				for(int i = 0; i < thrs.length; i++) {
					long t = thr.getThreadUserTime(thrs[i]);
					
					if(t >= 0L) {
						tot += t;
					}
				}
				
				return tot == 0L ? -1L : tot;
			} catch(SecurityException e) {
				return -1L;
			}
		} else {
			return -1L;
		}
	}
	
	static ThreadInfo[] getAllThreadInfos() {
		if(thr != null) {
			try {
				long[] thrs = thr.getAllThreadIds();
				
				ThreadInfo[] infos = new ThreadInfo[thrs.length];
				
				for(int i = 0; i < thrs.length; i++) {
					infos[i] = thr.getThreadInfo(thrs[i]);
				}
				
				return infos;
			} catch (SecurityException e) {
				return new ThreadInfo[] {};
			}
		}
		else {
			return new ThreadInfo[] {};
		}
	}
	
	static long getAllThreadBlockedCount() {
		if(thr != null && !thr.isThreadContentionMonitoringSupported()) {
			return -1L;
		}
		
		ThreadInfo[] infos = getAllThreadInfos();

		long tot = -1L;
		for(int i = 0; i < infos.length; i++) {
			if(infos[i] != null) {
				long data = infos[i].getBlockedCount();
				if(data != -1) {
					tot = (tot == -1L) ? data : (tot + data);
				}
			}
		}
		return tot;
	}
	
	static long getAllThreadBlockedTimeMillis() {
		if(thr != null && !thr.isThreadContentionMonitoringSupported()) {
			return -1L;
		}
		
		ThreadInfo[] infos = getAllThreadInfos();

		long tot = -1L;
		for(int i = 0; i < infos.length; i++) {
			if(infos[i] != null) {
				long data = infos[i].getBlockedTime();
				if(data != -1) {
					tot = (tot == -1L) ? data : (tot + data);
				}
			}
		}
		return tot;
	}
	
	static long getAllThreadWaitedCount() {
		if(thr != null && !thr.isThreadContentionMonitoringSupported()) {
			return -1L;
		}
		
		ThreadInfo[] infos = getAllThreadInfos();

		long tot = -1L;
		for(int i = 0; i < infos.length; i++) {
			if(infos[i] != null) {
				long data = infos[i].getWaitedCount();
				if(data != -1) {
					tot = (tot == -1L) ? data : (tot + data);
				}
			}
		}
		return tot;
	}
	
	static long getAllThreadWaitedTimeMillis() {
		if(thr != null && !thr.isThreadContentionMonitoringSupported()) {
			return -1L;
		}
		
		ThreadInfo[] infos = getAllThreadInfos();

		long tot = -1L;
		for(int i = 0; i < infos.length; i++) {
			if(infos[i] != null) {
				long data = infos[i].getWaitedTime();
				if(data != -1) {
					tot = (tot == -1L) ? data : (tot + data);
				}
			}
		}
		return tot;
	}
	
	static long getGCCollectionCounts() {
		if(gc != null && gc.size() > 0) {
			long total = 0L;
			
			for(GarbageCollectorMXBean gcb: gc) {
				total += gcb.getCollectionCount();
			}
			
			return total;
		}
		else {
			return -1L;
		}
	}
	
	static long getGCCollectionTimeMillis() {
		if(gc != null && gc.size() > 0) {
			long total = 0L;
			
			for(GarbageCollectorMXBean gcb: gc) {
				total += gcb.getCollectionTime();
			}
			
			return total;
		}
		else {
			return -1L;
		}
	}
	
	/**
	 * @return the number of processors available to the JVM
	 */
	static int getJVMAvailableProcessors() {
		if(os != null) {
			return os.getAvailableProcessors();
		}
		else {
			return -1;
		}
	}
	
	/**
	 * @return the operating system load average over the last minute.
	 */
	static double getLastMinuteSystemLoadAverage() {
		if(os != null) {
			return os.getSystemLoadAverage();
		}
		else {
			return -1.0;
		}
	}
	
	static String getHumanReadableBytes(long bytes) {
		String[] suffixes = {"b", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB", "ZiB", "YiB"};
		long divisor = 1024;
		int i = 0;
		long reported = bytes;
		
		while(bytes > divisor && divisor < Long.MAX_VALUE / 1024 && i + 1 < suffixes.length) {
			divisor *= 1024;
			if(reported % 1024L >= 512L) {
				reported /= 1024;
				reported++;
			}
			else {
				reported /= 1024;
			}
			i++;
		}
		
		return Long.toString(reported) + " " + suffixes[i];
	}
	
	static String getHumanReadableTime(long duration, long ns_per_unit_time) {
		long units_per_second = 1000000000L / ns_per_unit_time;
		long seconds = duration / units_per_second;
		long rem_ns = (duration - (seconds * units_per_second)) * ns_per_unit_time;
		long minutes = seconds / 60L;
		long rem_sec = seconds - (minutes * 60L);
		long hours = minutes / 60L;
		long rem_min = minutes - (hours * 60L);
		long days = hours / 24L;
		long rem_hours = hours - (days * 24L);
		String result = "";
		if(days > 0L) {
			result = String.format("%ldT%02ld:%02ld:%02ld.%09ld", days, rem_hours, rem_min, rem_sec, rem_ns);
		}
		else if(hours > 0L) {
			result = String.format("%ld:%02ld:%02ld.%09ld", hours, rem_min, rem_sec, rem_ns);
		}
		else if(minutes > 0L) {
			result = String.format("%ld:%02ld.%09ld", minutes, rem_sec, rem_ns);
		}
		else if(seconds > 0L) {
			result = String.format("%ld.%09ld", seconds, rem_ns);
		}
		else {
			result = String.format("0.%09ld", duration * ns_per_unit_time);
		}
		while(result.endsWith("0")) {
			result = result.substring(0, result.length() - 1);
		}
		return result.endsWith(".") ? result.substring(0, result.length() - 1) : result;
	}
}
