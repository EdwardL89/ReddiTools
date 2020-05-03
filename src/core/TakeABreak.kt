package core

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

private class TakeABreak: AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        BrowserUtil.browse("https://www.reddit.com/r/popular/")
    }

}