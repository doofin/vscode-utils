package vscutils
import typings.vscode.mod as vscode
import typings.vscode.anon.Dispose
import typings.vscode.mod.CompletionItemProvider
import typings.vscode.mod.ProviderResult

import scala.scalajs.js

object mCompletionItemProvider {

  def provider1 = {
    val provider1: CompletionItemProvider[vscode.CompletionItem] =
      new CompletionItemProvider {

        override def provideCompletionItems(
            document: vscode.TextDocument,
            position: vscode.Position,
            token: vscode.CancellationToken,
            context: vscode.CompletionContext
        ): ProviderResult[
          scala.scalajs.js.Array[vscode.CompletionItem] |
            vscode.CompletionList[vscode.CompletionItem]
        ] = {
          val items = Seq(1, 2, 3)
            .map(i =>
              new vscode.CompletionItem(s"Hello $i") {
                insertText = "helo"
                documentation = "documentation"
                sortText = i.toString()
              }
            )
            .reverse

          js.Array(items*)
        }

      }

    vscode.languages.registerCompletionItemProvider(
      "plaintext",
      provider1,
      "." // triggered whenever a '.' is being typed
    )
  }
}
