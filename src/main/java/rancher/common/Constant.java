package rancher.common;

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
		public static final String FINISHING_UPGRADE = "finishing-upgrade";
		public static final String UNKNOWN = "unknown";
		public static final String HEALTHY = "healthy";
		public static final String UNHEALTHY = "unhealthy";
		public static final String ACTIVATING = "activating";
	}

	public static final class Action {
		public static final String FINISH_UPGRAGE = "finishupgrade";
		public static final String UPGRAGE = "upgrade";
		public static final String ROLLBACK = "rollback";
	}

	public static final class QueryParam {
		public static final String ACTION = "action=";
		public static final String NAME = "name=";
		public static final String ALL = "all=";
	}

	public static final class PostDataTemplate {
		public static final String EMPTY = "{}";
		public static final String CREATE_SERVICE = "{\"name\":\"newServiceFromPlugin\", \"stackId\":\"1st20\", \"scale\":1, \"launchConfig\":{ \"tty\":true, \"vcpu\":1, \"imageUuid\":\"docker:10.3.65.122:5000/testhub:1.0.1\" }, \"assignServiceIpAddress\":false, \"startOnCreate\":true }";
		public static final String UPGRAGE_SERIVCE = "{ \"inServiceStrategy\":{ \"launchConfig\":{ \"imageUuid\":\"docker\" } }, \"toServiceStrategy\":null }";

	}

	public static final String RANCHER_URL = "http://10.3.65.122:8080/v2-beta/projects/1a5/services/1s63?action=";
	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";

	public static final String AUTHORIZATION = "Authorization";
	public static final String CONTENT_TYPE = "Content-Type";

	public static final String KEYWORD_STATE = "state";
	public static final String EMPTY_POST_DATA = "{}";

	public static final long REQUEST_INTERVAL = 3000;
	public static final String HTTP_PROTOCOL = "http";
	public static final CharSequence RANCHER_API_VERSION = "v2-beta";
	public static final String SERVICE_ID_NOT_EXISTED = "notExisted";

}
