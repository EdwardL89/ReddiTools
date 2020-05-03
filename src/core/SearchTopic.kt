package core

import com.intellij.ide.BrowserUtil
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor

private class SearchTopic: AnAction() {

    private lateinit var actionEvent: AnActionEvent

    override fun update(e: AnActionEvent) {
        val project = e.project
        var enablePlugin = false
        val editor = e.getData(CommonDataKeys.EDITOR)
        if (editor != null && project != null) enablePlugin = editor.caretModel.allCarets.isNotEmpty()
        e.presentation.isEnabledAndVisible = enablePlugin
    }

    override fun actionPerformed(e: AnActionEvent) {
        actionEvent = e
        val editor = e.getData(CommonDataKeys.EDITOR)
        val hasSelection = editor?.selectionModel?.hasSelection()
        if (!hasSelection!!) showErrorNotification()
        else searchOnReddit(editor)
    }

    private fun searchOnReddit(editor: Editor) {
        val caretModel = editor.caretModel
        val selectedText = caretModel.currentCaret.selectedText!!
        val currentLanguage = getCurrentLanguage()
        BrowserUtil.browse("https://www.reddit.com$currentLanguage/search?q=$selectedText&restrict_sr=1")
    }

    private fun getCurrentLanguage(): String {
        val currentLanguage = actionEvent.getData(CommonDataKeys.PSI_FILE)?.language?.displayName!!.toLowerCase()
        var subreddit = "/r/$currentLanguage"
        if (currentLanguage.isEmpty() || currentLanguage == "plain text") subreddit = ""
        return subreddit
    }

    private fun showErrorNotification() {
        val notification = NotificationGroup("ReddiTools", NotificationDisplayType.BALLOON, true)
        notification.createNotification("ReddiTools",
                "Nothing highlighted: Highlight text you'd like to search",
                NotificationType.INFORMATION,
                null).notify(actionEvent.project)
    }

}