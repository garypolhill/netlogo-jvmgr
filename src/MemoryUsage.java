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
public class MemoryUsage implements Reporter {
	public enum UsageCommand { USED_BYTES, USED_PROP, USED_PCT_STR, USED_HEAP, USED_NON_HEAP,
		COMMITTED_HEAP, COMMITTED_NON_HEAP, INIT_HEAP, INIT_NON_HEAP, MAX_HEAP,
		MAX_NON_HEAP, USED_HR, USED_HEAP_HR, USED_NON_HEAP_HR, COMMITTED_HR };
	private UsageCommand cmd;
	
	MemoryUsage(UsageCommand cmd) {
		this.cmd = cmd;
	}

	@Override
	public Syntax getSyntax() {
		switch(cmd) {
		case USED_BYTES:
		case USED_PROP:
		case USED_HEAP:
		case USED_NON_HEAP:
		case COMMITTED_HEAP:
		case COMMITTED_NON_HEAP:
		case INIT_HEAP:
		case INIT_NON_HEAP:
		case MAX_HEAP:
		case MAX_NON_HEAP:
			return SyntaxJ.commandSyntax(new int[] {Syntax.VoidType()}, Syntax.NumberType());
		case USED_PCT_STR:
		case USED_HR:
		case USED_HEAP_HR:
		case USED_NON_HEAP_HR:
		case COMMITTED_HR:
			return SyntaxJ.commandSyntax(new int[] {Syntax.VoidType()}, Syntax.StringType());
		default:
			throw new RuntimeException("BUG!");
		}
	}

	@Override
	public Object report(Argument[] args, Context context) throws ExtensionException {
		long heap_used, non_heap_used, heap_committed, non_heap_committed, heap_max,
		non_heap_max, heap_init, non_heap_init;

		switch(cmd) {			
		case USED_BYTES:
			heap_used = JVMGRExtension.getHeapUsed();
			non_heap_used = JVMGRExtension.getNonHeapUsed();
			if(heap_used == -1L || non_heap_used == -1L) {
				return Double.NaN;
			}
			return new Double(heap_used + non_heap_used);
		case USED_PROP:
			heap_used = JVMGRExtension.getHeapUsed();
			non_heap_used = JVMGRExtension.getNonHeapUsed();
			heap_committed = JVMGRExtension.getHeapCommitted();
			non_heap_committed = JVMGRExtension.getNonHeapCommitted();
			if(heap_used == -1L || non_heap_used == -1L
					|| heap_committed == -1L || non_heap_committed == -1L) {
				return Double.NaN;
			}
			return new Double((double)(heap_used + non_heap_used)
					/ (double)(heap_committed + non_heap_committed));
		case USED_PCT_STR:
			heap_used = JVMGRExtension.getHeapUsed();
			non_heap_used = JVMGRExtension.getNonHeapUsed();
			heap_committed = JVMGRExtension.getHeapCommitted();
			non_heap_committed = JVMGRExtension.getNonHeapCommitted();
			if(heap_used == -1L || non_heap_used == -1L
					|| heap_committed == -1L || non_heap_committed == -1L) {
				return Double.NaN;
			}
			return String.format("%g", Math.round((100.0 * (double)(heap_used + non_heap_used))
					/ (double)(heap_committed + non_heap_committed)));
		case USED_HEAP:
			heap_used = JVMGRExtension.getHeapUsed();
			return heap_used == -1L ? Double.NaN : new Double(heap_used);
		case USED_NON_HEAP:
			non_heap_used = JVMGRExtension.getNonHeapUsed();
			return non_heap_used == -1L ? Double.NaN : new Double(non_heap_used);
		case COMMITTED_HEAP:
			heap_committed = JVMGRExtension.getHeapCommitted();
			return heap_committed == -1L ? Double.NaN : new Double(heap_committed);
		case COMMITTED_NON_HEAP:
			non_heap_committed = JVMGRExtension.getNonHeapCommitted();
			return non_heap_committed == -1L ? Double.NaN : new Double(non_heap_committed);
		case INIT_HEAP:
			heap_init = JVMGRExtension.getHeapInitial();
			return heap_init == -1L ? Double.NaN : new Double(heap_init);
		case INIT_NON_HEAP:
			non_heap_init = JVMGRExtension.getNonHeapInitial();
			return non_heap_init == -1L ? Double.NaN : new Double(non_heap_init);
		case MAX_HEAP:
			heap_max = JVMGRExtension.getHeapMax();
			return heap_max == -1L ? Double.NaN : new Double(heap_max);
		case MAX_NON_HEAP:
			non_heap_max = JVMGRExtension.getNonHeapMax();
			return non_heap_max == -1L ? Double.NaN : new Double(non_heap_max);
		case USED_HR:
			heap_used = JVMGRExtension.getHeapUsed();
			non_heap_used = JVMGRExtension.getNonHeapUsed();
			if(heap_used == -1L || non_heap_used == -1L) {
				return "NA";
			}
			return JVMGRExtension.getHumanReadableBytes(heap_used + non_heap_used);
		case USED_HEAP_HR:
			heap_used = JVMGRExtension.getHeapUsed();
			if(heap_used == -1L) {
				return "NA";
			}
			return JVMGRExtension.getHumanReadableBytes(heap_used);
		case USED_NON_HEAP_HR:
			non_heap_used = JVMGRExtension.getNonHeapUsed();
			if(non_heap_used == -1L) {
				return "NA";
			}
			return JVMGRExtension.getHumanReadableBytes(non_heap_used);
		case COMMITTED_HR:
			heap_committed = JVMGRExtension.getHeapCommitted();
			non_heap_committed = JVMGRExtension.getNonHeapCommitted();
			if(heap_committed == -1L || non_heap_committed == -1L) {
				return "NA";
			}
			return JVMGRExtension.getHumanReadableBytes(heap_committed + non_heap_committed);
		default:
			throw new ExtensionException("BUG!: MemoryUsage created with unrecognized cmd option");
		}
	}
	

}
