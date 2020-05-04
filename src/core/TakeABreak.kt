package core

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.notification.NotificationType
import com.intellij.notification.NotificationGroup
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.notification.NotificationDisplayType

private class TakeABreak: AnAction() {

    private lateinit var actionEvent: AnActionEvent

    override fun actionPerformed(e: AnActionEvent) {
        actionEvent = e
        val editor = e.getData(CommonDataKeys.EDITOR)
        if (editor != null && e.project != null) {
            val hasSelection = editor.selectionModel.hasSelection()
            if (!hasSelection) BrowserUtil.browse("https://www.reddit.com/r/popular/")
            else openSubreddit(editor)
        } else {
            BrowserUtil.browse("https://www.reddit.com/r/popular/")
        }
    }

    private fun openSubreddit(editor: Editor) {
        val caretModel = editor.caretModel
        val selectedText = caretModel.currentCaret.selectedText!!
        if (selectedText.length > 2 && selectedText.take(2) != "r/") showErrorNotification("Missing Prefix: Prefix the subreddit with \"r/\"")
        else if (selectedText.length < 3) showErrorNotification("Missing subreddit or prefix: Please type \"r/\" followed by the subreddit")
        else BrowserUtil.browse("https://www.reddit.com/$selectedText/")
    }

    private fun showErrorNotification(message: String) {
        val notification = NotificationGroup("ReddiTools", NotificationDisplayType.BALLOON, true)
        notification.createNotification("ReddiTools",
                message,
                NotificationType.INFORMATION,
                null).notify(actionEvent.project)
    }
}