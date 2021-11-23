package com.example.tumiweb.services;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IBackupService {
    boolean backupUser(HttpServletResponse res);
    boolean backupAnswer(HttpServletResponse res);
    boolean backupQuestionByChapterId(HttpServletResponse res, Long chapterId) throws IOException;
    boolean backupCategory(HttpServletResponse res);
    boolean backupChapter(HttpServletResponse res);
    boolean backupDiary(HttpServletResponse res);
    boolean backupGift(HttpServletResponse res);
    boolean backupHelp(HttpServletResponse res);
    boolean backupNotification(HttpServletResponse res);
    boolean backupCourse(HttpServletResponse res);
}
