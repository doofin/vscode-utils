package vscutils

import typings.vscode.mod as vscode
import typings.vscode.anon.Dispose

import scala.collection.immutable
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.concurrent.ExecutionContext.Implicits.global

import typings.node.readlineMod
import typings.node.streamMod
import typings.node.NodeJS.ReadableStream
import typings.node.global.console

import typings.node.processMod

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

  // detect keys in nodejs api
  def checkKeyNodejs() = {
    /* const readline = require('readline');
const { Readable } = require('stream');

const inStream = new Readable({
  read() { console.log('in reading'); }
});

let i = 0
setInterval(() => { inStream.push(`${i++}`) }, 1000)
readline.emitKeypressEvents(inStream);

inStream.on('keypress', (...ar) => {
  console.log(ar)
});
     */
    val inStream = new streamMod.Readable {
      def read() = { console.log("read") }
    }.asInstanceOf[ReadableStream]

    inStream.on(
      "keypress",
      { x => vscode.window.showInformationMessage(s"data: ${x}") }
    )
    val stdin1 = processMod.global.process.stdin
    val iff = readlineMod.createInterface(stdin1.asInstanceOf[ReadableStream])
    readlineMod.emitKeypressEvents(inStream, iff)
    inStream.emit("keypress", "data")
  }
}
