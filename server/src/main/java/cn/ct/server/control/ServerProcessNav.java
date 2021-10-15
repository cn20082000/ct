/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.server.control;

import cn.ct.model.ObjectWrapper;

/**
 *
 * @author cn200
 */
public interface ServerProcessNav {
    public ObjectWrapper resSignIn(ObjectWrapper data);
    public ObjectWrapper resSignUp(ObjectWrapper data);
    public ObjectWrapper resUpdate(ObjectWrapper data);
    
    public ObjectWrapper resCreateProject(ObjectWrapper data);
    public ObjectWrapper resListProject(ObjectWrapper data);
    public ObjectWrapper resOpenProject(ObjectWrapper data);
    public ObjectWrapper resCloseProject(ObjectWrapper data);
    public ObjectWrapper resCollabProject(ObjectWrapper data);
    
    public ObjectWrapper resSendCollab(ObjectWrapper data);
    public void sendReCollab(ObjectWrapper data);
    public ObjectWrapper resListCollab(ObjectWrapper data);
    public ObjectWrapper resAcceptCollab(ObjectWrapper data);
    public ObjectWrapper resRejectCollab(ObjectWrapper data);
    public void sendLiCollab(ObjectWrapper data);
    public void sendLiOnline(ObjectWrapper data);
}
