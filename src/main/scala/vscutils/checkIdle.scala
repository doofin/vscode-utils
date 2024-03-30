package vscutils

import typings.vscode.mod as vscode
import typings.vscode.anon.Dispose

import scala.collection.immutable
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.concurrent.ExecutionContext.Implicits.global

/* https://stackoverflow.com/questions/57561242/vscode-extension-how-to-log-keystrokes
 */
object checkIdle {
  var lastVersionOpt: Option[Double] = None

  def runOnUnchanged(context: vscode.ExtensionContext, f: => Unit) = {
    // vscode.window.showInformationMessage("check isIdle")
    val editor = vscode.window.activeTextEditor;

    val version = editor.get.document.version;
    lastVersionOpt = Some(version)
    val disp = vscode.workspace
      .onDidChangeTextDocument(
        { edt =>
          // vscode.window.showInformationMessage(s"onDidChangeTextDocument")
          js.timers.setTimeout(1000) {
            val newVersion = editor.get.document.version;
            // vscode.window.showInformationMessage(s"last,new: $lastVersionOpt , $newVersion")
            val notChanged = for { lastVersion <- lastVersionOpt } yield (lastVersion == newVersion)

            if notChanged == Some(true) then f

            lastVersionOpt = Some(newVersion)
          }
        },
        null,
        null
      )
      .asInstanceOf[Dispose]

    context.subscriptions.push(disp)

  }
}
