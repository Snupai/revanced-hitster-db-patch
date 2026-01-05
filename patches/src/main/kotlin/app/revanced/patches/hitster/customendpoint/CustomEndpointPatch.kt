package app.revanced.patches.hitster.customendpoint

import app.revanced.patcher.fingerprint
import app.revanced.patcher.patch.bytecodePatch
import app.revanced.patcher.extensions.InstructionExtensions.replaceInstruction
import app.revanced.patcher.patch.PatchException
import app.revanced.patcher.patch.options.StringOption
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21c
import com.android.tools.smali.dexlib2.iface.reference.StringReference

internal val baseUrlFingerprint = fingerprint {
    strings("https://hitster.jumboplay.com/hitster-assets/")
}

object CustomEndpointOptions {
    val customEndpointUrl = StringOption(
        key = "custom-endpoint-url",
        title = "Custom endpoint URL",
        description = "The base URL for gameset_database.json and other config files. Must end with a forward slash (/).",
        default = "https://hitster.jumboplay.com/hitster-assets/"
    )
}

@Suppress("unused")
val customEndpointPatch = bytecodePatch(
    name = "Custom gameset database endpoint",
    description = "Allows setting a custom endpoint URL for gameset_database.json and other config files.",
    use = true
) {
    compatibleWith("nl.jumbo.hitster")

    execute {
        val method = baseUrlFingerprint.method
        val instructions = method.implementation?.instructions
            ?: throw PatchException("Method implementation not found")

        var foundIndex = -1
        var targetRegister = -1

        instructions.forEachIndexed { index, instruction ->
            if (instruction is Instruction21c) {
                val reference = instruction.reference
                if (reference is StringReference && reference.string == "https://hitster.jumboplay.com/hitster-assets/") {
                    foundIndex = index
                    targetRegister = (instruction as OneRegisterInstruction).registerA
                    return@forEachIndexed
                }
            }
        }

        if (foundIndex == -1) {
            throw PatchException("Could not find base URL string constant")
        }

        // Use the option value for the custom URL
        val customUrl = CustomEndpointOptions.customEndpointUrl.get()

        // Replace the string constant with our custom URL
        method.replaceInstruction(
            foundIndex,
            "const-string v$targetRegister, \"$customUrl\""
        )
    }
}
