package ct.tcp.control;

import ct.model.ObjectWrapper;

public interface ServerProcessNav {
    ObjectWrapper replyTester(ObjectWrapper data);
    ObjectWrapper replySignIn(ObjectWrapper data);
    ObjectWrapper replySignUp(ObjectWrapper data);
    ObjectWrapper replyChangePassword(ObjectWrapper data);
    ObjectWrapper replySignOut(ObjectWrapper data);

    ObjectWrapper replyCreateProject(ObjectWrapper data);
    ObjectWrapper replyListProject(ObjectWrapper data);
    ObjectWrapper replyCollabProject(ObjectWrapper data);
    ObjectWrapper replyOpenProject(ObjectWrapper data);
    void syncJoinRoom(Object data);
    ObjectWrapper replyCloseProject(ObjectWrapper data);
    void syncUpdateProject(Object data);

    ObjectWrapper replySendCollab(ObjectWrapper data);
    ObjectWrapper replyListCollab(ObjectWrapper data);
    ObjectWrapper replyAcceptCollab(ObjectWrapper data);
    ObjectWrapper replyRejectCollab(ObjectWrapper data);
    void syncReceiveCollab(Object data);
    ObjectWrapper replySearchUser(ObjectWrapper data);

    ObjectWrapper replyOpenFile(ObjectWrapper data);
    void syncChangedText(Object data);
    ObjectWrapper replyChangeText(ObjectWrapper data);
    ObjectWrapper replyRunProject(ObjectWrapper data);
    ObjectWrapper replyCreateFile(ObjectWrapper data);
    ObjectWrapper replyDownloadFile(ObjectWrapper data);
}
