package core

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

private class SubmitPost: AnAction() {

    override fun update(e: AnActionEvent) {
        val project = e.project
        var enablePlugin = false
        val editor = e.getData(CommonDataKeys.EDITOR)
        if (editor != null && project != null) enablePlugin = editor.caretModel.allCarets.isNotEmpty()
        e.presentation.isEnabledAndVisible = enablePlugin
    }

    override fun actionPerformed(e: AnActionEvent) {
        val currentLanguage = e.getData(CommonDataKeys.PSI_FILE)?.language?.displayName!!.toLowerCase()
        var subreddit = "/r/$currentLanguage"
        if (currentLanguage.isEmpty()) subreddit = ""
        BrowserUtil.browse("https://www.reddit.com$subreddit/submit")
    }

}