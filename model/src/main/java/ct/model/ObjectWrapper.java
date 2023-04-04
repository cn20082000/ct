package ct.model;

import java.io.Serializable;

public class ObjectWrapper implements Serializable {
    /** Test label, nothing to process */
    public static final int LBL_TESTER = 0;
    /** Client -> Server: Send data to server and no need response */
    public static final int LBL_POST = 1;
    /** Client -> Server: Request get data from server once */
    public static final int LBL_GET = 2;

    /** Server -> Client: Data is constantly updated from the server */
    public static final int LBL_SYNC = 3;
    /** Server -> Client: Post or get success */
    public static final int LBL_SUCCESS = 10;
    /** Server -> Client: Post or get failed, message in data */
    public static final int LBL_FAILED = 11;

    /** Test request, nothing to process */
    public static final int TESTER = 0;
    /** Get: sign in<br>
     * Send: User(username, password)<br>
     * Receive: User(name, username) */
    public static final int SIGN_IN = 1001;
    /** Post: sign up<br>
     * Send: User(name, username, password) */
    public static final int SIGN_UP = 1002;
    /** Post: change password<br>
     * Send: User(password) */
    public static final int CHANGE_PASSWORD = 1003;
    /** Post: sign out<br>
     * Send: null */
    public static final int SIGN_OUT = 1004;

    /** Get: create new project<br>
     * Send: String: project name<br>
     * Receive: Project(full) */
    public static final int CREATE_PROJECT = 1101;
    /** Get: get all my project<br>
     * Send: null<br>
     * Receive: List(Project)*/
    public static final int LIST_PROJECT = 1102;
    /** Get: get all my collab project<br>
     * Send: null<br>
     * Receive: List(Project)*/
    public static final int COLLAB_PROJECT = 1103;
    /** Get: open a project and notify to other in room<br>
     * Send: Project<br>
     * Receive: Project */
    public static final int OPEN_PROJECT = 1104;
    /** Sync: receive all members in current room<br>
     * Receive: List(User) */
    public static final int JOIN_ROOM = 1105;
    /** Post: close project and notify to other in room<br>
     * Send: null */
    public static final int CLOSE_PROJECT = 1106;
    /** Sync: receive update project and reload view
     * Receive: Project */
    public static final int UPDATE_PROJECT = 1107;

    /** Post: send collab to other user<br>
     * Send: Collab(user, project) */
    public static final int SEND_COLLAB = 1201;
    /** Get: get all collab send to myself<br>
     * Send: null<br>
     * Receive: List(Collab) */
    public static final int LIST_COLLAB = 1202;
    /** Post: Accept collab from other user<br>
     * Send: Collab(user, project) */
    public static final int ACCEPT_COLLAB = 1203;
    /** Post: Reject collab from other user<br>
     * Send: Collab(user, project) */
    public static final int REJECT_COLLAB = 1204;
    /** Sync: a notification<br>
     * Receive: Collab */
    public static final int RECEIVE_COLLAB = 1205;
    /** Get: Notice just received an invitation<br>
     * Send: String - key<br>
     * Receive: List(User) */
    public static final int SEARCH_USER = 1206;

    /** Get: Open a file and get file data<br>
     * Send: File<br>
     * Receive: String*/
    public static final int OPEN_FILE = 1301;
    /** Sync: Send change of text to client<br>
     * Receive: ChangedText */
    public static final int CHANGED_TEXT = 1302;
    /** Post: Send change of text to server<br>
     * Send: ChangedText */
    public static final int CHANGE_TEXT = 1303;
    /** Get: Run project and get result<br>
     * Send: String - input<br>
     * Receive: String - output*/
    public static final int RUN_PROJECT = 1304;
    /** Post: Create new file<br>
     * Send: File */
    public static final int CREATE_FILE = 1305;
    /** Get: Get file data only<br>
     * Send: File<br>
     * Receive: String*/
    public static final int DOWNLOAD_FILE = 1306;

    private int label;
    private int performative;
    private Object data;

    public ObjectWrapper() {
        super();
    }

    public ObjectWrapper(int label, int performative, Object data) {
        super();
        this.label = label;
        this.performative = performative;
        this.data = data;
    }

    public int getLabel() {
        return label;
    }

    public int getPerformative() {
        return performative;
    }

    public Object getData() {
        return data;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public void setPerformative(int performative) {
        this.performative = performative;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static class Builder {
        private ObjectWrapper obj;

        public Builder() {
            obj = new ObjectWrapper(ObjectWrapper.LBL_TESTER, ObjectWrapper.TESTER, null);
        }

        public Builder setLabel(int label) {
            obj.setLabel(label);
            return this;
        }

        public Builder setPerformative(int performative) {
            obj.setPerformative(performative);
            return this;
        }

        public Builder setData(Object data) {
            obj.setData(data);
            return this;
        }

        public ObjectWrapper build() {
            return obj;
        }
    }
}
