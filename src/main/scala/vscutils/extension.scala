package vscutils

import typings.vscode.mod as vscode
import typings.vscode.anon.Dispose

import scala.collection.immutable
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel
import vscutils.checkIdle.checkKeyNodejs

object extension {

  /** This method is called when your extension is actived. The extension is activated the first
    * time one of its features is used (here we only have commands).
    */
  @JSExportTopLevel("activate") // Exports the function to javascript so that VSCode can load it
  def activate(context: vscode.ExtensionContext): Unit = {
    vscode.window.showInformationMessage("your extension vscode -scalajs is now active!")

    checkKeyNodejs()
    // Store all the commands here
    val commands = Seq(
      ("extension.helloWorld", showHello),
      ("extension.fmt", format.doFormat)
    )

    val lsr = ChildProcess.execSync("ls")
    vscode.window.showInformationMessage(s"lsr : ${lsr}")
    // Register the commands in VSCode
    commands foreach { (name, fun) =>
      context.subscriptions.push(
        vscode.commands
          .registerCommand(name, fun)
          .asInstanceOf[Dispose]
          // to make the typechecker happy (VSCode has typescript facades nowadays)
      )
    }
    context.subscriptions.push(mCompletionItemProvider.provider1.asInstanceOf[Dispose])

    vscode.commands.registerCommand(
      "type",
      { e => vscode.window.showInformationMessage(s"evt: $e ") }
    )
    checkIdle.runOnUnchanged(
      context, {
        // vscode.window.showInformationMessage(s"un Changed")
      }
    )
  }

  /** Example command. VSCode commands can take an argument of any type, hence the `Any` here.
    *
    * @param arg
    *   the argument (which we don't use, but it could be useful for other commands)
    */
  def showHello(arg: Any): Unit = {
    vscode.window.showInformationMessage(s"Hello World! How are you ?")
  }
}
