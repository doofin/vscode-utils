package vscutils

import typings.vscode.mod as vscode
import typings.vscode.anon.Dispose

import scala.collection.immutable
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.concurrent.ExecutionContext.Implicits.global

object format {
  def doFormat(arg: Any) = {
    val editor = vscode.window.activeTextEditor;
    vscode.workspace.findFiles("*.scala", "**/node_modules/**")
    if (editor.isDefined) {
      val doc = editor.get.document
      val options = vscode.workspace.getConfiguration("editor");

      val cmd = "editor.action.formatDocument"
      //   "vscode.executeFormatDocumentProvider"
      vscode.window.showInformationMessage(s"formatted")
      vscode.commands
        .executeCommand(cmd, doc, options)
        .toFuture onComplete { x => }
    }
  }
}
