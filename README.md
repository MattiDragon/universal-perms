# Universal Perms
Have you ever had to install mod that doesn't provide permission nodes for its commands on a server? 
Have you had trouble with a mod not allowing you to block access to their root command (looking at you luckperms)? 
Well, this mod fixes that, and more.

## Usage
First, you'll need to install another mod to manage permissions for you. I recommend luckperms, but player roles should work just as well.
Before you start using this mod, I recommend logging in once so that luckperms gets to know all the nodes, so you can see them in the web editor.

Now we get to setting up the permissions. This mod provides two permissions for each command, one beginning with `universal_perms.use` and one with `universal_perms.view`.

If set, the `use` permissions overrides the commands normal requirements making anyone with it set to true able to use the command while denying access to anyone with it set to false.
The `view` permissions is useful if you just want to hide the autocompletion, but still allow functionality for macros or something else.

For all vanilla commands (yes, even `execute`), subcommands and arguments are visible as their own sub-nodes, for example, to disable the use of `/help <command>`, but not `/help` you would use the `universal_perms.use.help.command` node. However, this doesn't work with mods that use their own subcommand system (why luckperms?).

You can even affect all commands by using `universal_perms.use` and `universal_perms.view` as permission nodes, but I don't really see the benefit in that.

## Pitfalls and Quirks
1. The `view` permission has power over the `use` one when it comes to sending the client the available commands. This means that you can create scenarios where a command is visible, but unusable.
2. Commands that are aliases to other commands behave weirdly when their target is forbidden/hidden, but they are visible. Here is a list of vanilla aliases to watch out for:
    * `tell` and `w` redirect to `msg`
    * `tp` redirects to `teleport`
    * `xp` redirects to `experience`
    * `tm` redirects to `teammsg`
3. Compatibility with other mods isn't guaranteed if the other mods add their commands later than usual or mess with their requirements after universal perms does.
