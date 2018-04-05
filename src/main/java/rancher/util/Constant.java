package rancher.util;

public class Constant {

	private Constant() {
		throw new IllegalStateException("Constant class");
	}

	public static final class State {

		private State() {
			throw new IllegalStateException("State class");
		}

		public static final String ACTIVE = "active";
		public static final String UPGRADED = "upgraded";
		public static final String UPGRADING = "upgrading";
		public static final String CANCEL = "upgraded";
		public static final String ROLLING_BACK = "rolling-back";

		public static final String UNKNOWN = "unknown";
		public static final String HEALTHY = "healthy";
		public static final String UNHEALTHY = "unhealthy";

	}

	public static final class Action {
		public static final String FINISH_UPGRAGE = "finishupgrade";
		public static final String UPGRAGE = "upgrade";
		public static final String ROLLBACK = "rollback";
	}	
	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";

	public static final String AUTHORIZATION = "Authorization";
	public static final String CONTENT_TYPE = "Content-Type";

}
