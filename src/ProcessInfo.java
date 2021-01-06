import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.Reporter;
import org.nlogo.core.Syntax;
import org.nlogo.core.SyntaxJ;

/**
 * 
 */

/**
 * @author gary
 *
 */
public class ProcessInfo implements Reporter {
	public enum InfoCommand { THREADS, PEAK_THREADS, ALL_THREADS, THREAD_CPU, THREAD_USER,
		THREAD_SYSTEM, CPU_TIME, USER_TIME, SYSTEM_TIME, BLOCKED_TIME, BLOCKED_COUNT, WAITED_TIME,
		WAITED_COUNT, JVM_START_TIME, JVM_UPTIME, GC_COUNT, GC_TIME, JVM_PROC_COUNT,
		SYSTEM_LOAD, JVM_NAME, CPU_TIME_STR, USER_TIME_STR, SYSTEM_TIME_STR, WAITED_TIME_STR,
		BLOCKED_TIME_STR, JVM_UPTIME_STR };
	private InfoCommand cmd;
	
	ProcessInfo(InfoCommand cmd) {
		this.cmd = cmd;
	}
		
	@Override
	public Syntax getSyntax() {
		switch(cmd) {
		case THREADS:
		case PEAK_THREADS:
		case ALL_THREADS:
		case THREAD_CPU:
		case THREAD_USER:
		case THREAD_SYSTEM:
		case CPU_TIME:
		case USER_TIME:
		case SYSTEM_TIME:
		case BLOCKED_TIME:
		case BLOCKED_COUNT:
		case WAITED_TIME:
		case WAITED_COUNT:
		case JVM_START_TIME:
		case JVM_UPTIME:
		case GC_COUNT:
		case GC_TIME:
		case JVM_PROC_COUNT:
		case SYSTEM_LOAD:
			return SyntaxJ.reporterSyntax(Syntax.NumberType(), "O---");
		case JVM_NAME:
		case CPU_TIME_STR:
		case USER_TIME_STR:
		case SYSTEM_TIME_STR:
		case WAITED_TIME_STR:
		case BLOCKED_TIME_STR:
		case JVM_UPTIME_STR:
			return SyntaxJ.reporterSyntax(Syntax.StringType(), "O---");
		default:
			throw new RuntimeException("BUG!");
		}
	}

	@Override
	public Object report(Argument[] args, Context context) throws ExtensionException {
		String name;
		long uptime, start_time, started_threads, cpu_time_cur, user_time_cur, cpu_time_all,
			user_time_all, blocked_count, blocked_time, waited_count, waited_time, gc_counts,
			gc_time;
		int thread_count, peak_thread_count, jvm_proc;
		double system_load;
		
		switch(cmd) {
		case THREADS:
			thread_count = JVMGRExtension.getLiveThreadCount();
			return thread_count == -1 ? Double.NaN : new Double(thread_count);
		case PEAK_THREADS:
			peak_thread_count = JVMGRExtension.getPeakThreadCount();
			return peak_thread_count == -1 ? Double.NaN : new Double(peak_thread_count);
		case ALL_THREADS:
			started_threads = JVMGRExtension.getTotalStartedThreadCount();
			return started_threads == -1L ? Double.NaN : new Double(started_threads);
		case THREAD_CPU:
			cpu_time_cur = JVMGRExtension.getCurrentThreadCpuTimeNanos();
			return cpu_time_cur == -1L ? Double.NaN : new Double((double)cpu_time_cur / 1.0e9);
		case THREAD_USER:
			user_time_cur = JVMGRExtension.getCurrentThreadUserTimeNanos();
			return user_time_cur == -1L ? Double.NaN : new Double((double)user_time_cur / 1.0e9);
		case THREAD_SYSTEM:
			cpu_time_cur = JVMGRExtension.getCurrentThreadCpuTimeNanos();
			user_time_cur = JVMGRExtension.getCurrentThreadUserTimeNanos();
			if(cpu_time_cur == -1L || user_time_cur == -1L) {
				return Double.NaN;
			}
			return new Double((double)(cpu_time_cur - user_time_cur) / 1.0e9);
		case CPU_TIME:
			cpu_time_all = JVMGRExtension.getAllThreadCpuTimeNanos();
			return cpu_time_all == -1L ? Double.NaN : new Double((double)cpu_time_all / 1.0e9);
		case USER_TIME:
			user_time_all = JVMGRExtension.getAllThreadUserTimeNanos();
			return user_time_all == -1L ? Double.NaN : new Double((double)user_time_all / 1.0e9);
		case SYSTEM_TIME:
			cpu_time_all = JVMGRExtension.getAllThreadCpuTimeNanos();
			user_time_all = JVMGRExtension.getAllThreadUserTimeNanos();
			if(cpu_time_all == -1L || user_time_all == -1L) {
				return Double.NaN;
			}
			return new Double((double)(cpu_time_all - user_time_all) / 1.0e9);
		case BLOCKED_TIME:
			blocked_time = JVMGRExtension.getAllThreadBlockedTimeMillis();
			return blocked_time == -1L ? Double.NaN : new Double((double)blocked_time / 1.0e3);
		case BLOCKED_COUNT:
			blocked_count = JVMGRExtension.getAllThreadBlockedCount();
			return blocked_count == -1L ? Double.NaN : new Double(blocked_count);
		case WAITED_TIME:
			waited_time = JVMGRExtension.getAllThreadWaitedTimeMillis();
			return waited_time == -1L ? Double.NaN : new Double((double)waited_time / 1.0e3);
		case WAITED_COUNT:
			waited_count = JVMGRExtension.getAllThreadWaitedCount();
			return waited_count == -1L ? Double.NaN : new Double(waited_count);
		case JVM_START_TIME:
			start_time = JVMGRExtension.getJVMStartTimeMillis();
			return start_time == -1L ? Double.NaN : new Double((double)start_time / 1.0e3);
		case JVM_UPTIME:
			uptime = JVMGRExtension.getJVMUptimeMillis();
			return uptime == -1L ? Double.NaN : new Double((double)uptime / 1.0e3);
		case GC_COUNT:
			gc_counts = JVMGRExtension.getGCCollectionCounts();
			return gc_counts == -1L ? Double.NaN : new Double(gc_counts);
		case GC_TIME:
			gc_time = JVMGRExtension.getGCCollectionTimeMillis();
			return gc_time == -1L ? Double.NaN : new Double((double)gc_time / 1.0e3);
		case JVM_PROC_COUNT:
			jvm_proc = JVMGRExtension.getJVMAvailableProcessors();
			return jvm_proc == -1 ? Double.NaN : new Double(jvm_proc);
		case SYSTEM_LOAD:
			system_load = JVMGRExtension.getLastMinuteSystemLoadAverage();
			return system_load == -1.0 ? Double.NaN : system_load;
		case JVM_NAME:
			name = JVMGRExtension.getJVMName();
			return name == null ? "NA" : name;
		case CPU_TIME_STR:
			cpu_time_all = JVMGRExtension.getAllThreadCpuTimeNanos();
			return cpu_time_all == -1L ? "NA" : JVMGRExtension.getHumanReadableTime(cpu_time_all, 1L);
		case USER_TIME_STR:
			user_time_all = JVMGRExtension.getAllThreadUserTimeNanos();
			return user_time_all == -1L ? "NA" : JVMGRExtension.getHumanReadableTime(user_time_all, 1L);
		case SYSTEM_TIME_STR:
			cpu_time_all = JVMGRExtension.getAllThreadCpuTimeNanos();
			user_time_all = JVMGRExtension.getAllThreadUserTimeNanos();
			if(cpu_time_all == -1L || user_time_all == -1L) {
				return "NA";
			}
			return JVMGRExtension.getHumanReadableTime(cpu_time_all - user_time_all, 1L);
		case WAITED_TIME_STR:
			waited_time = JVMGRExtension.getAllThreadWaitedTimeMillis();
			return waited_time == -1L ? "NA" : JVMGRExtension.getHumanReadableTime(waited_time, 1000000L);
		case BLOCKED_TIME_STR:
			blocked_time = JVMGRExtension.getAllThreadBlockedTimeMillis();
			return blocked_time == -1L ? "NA" : JVMGRExtension.getHumanReadableTime(blocked_time, 1000000L);
		case JVM_UPTIME_STR:
			uptime = JVMGRExtension.getJVMUptimeMillis();
			return uptime == -1L ? "NA" : JVMGRExtension.getHumanReadableTime(uptime, 1000000L);
		default:
			throw new ExtensionException("BUG!: ProcessInfo created with unrecognized cmd option");
		}
	}

}
