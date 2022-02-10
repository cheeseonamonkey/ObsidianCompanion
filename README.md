# ObsidianCompanion
Android companion app to Obsidian.MD

## Settings Screen

Settings screen

![[Pasted image 20220129143435.png]]



### Request permissions
You will be brought to file permissions in your settings

![[Screenshot_20220129-054044_Settings.png]]

Enable ObsidianCompanion on this setting page.

![[Screenshot_20220129-054047_Settings.png]]

You will also be asked for file write acess.


### Set default QuickAdd file
Select the default target file in vault for QuickAdd in user file explorer.


### Post-processing

Used to template your QuickAdd data.

Add new lines, variables, and markdown text.

#### prepend
This is added *before* the QuickAdd data .

![[Screenshot_20220129-054147_ObsidianCompanion.png]]

#### append
This is added *after* the QuickAdd data .

![[Screenshot_20220129-054153_ObsidianCompanion.png]]

### Variables

Variables will be replaced in post-processing:
- `[!empty]`
- `[!time]`
- `[!date]`




## QuickAdd Usage

Gif usage example
-GIF EXAMPLE-

1. *Enable required permissions*
2. *Set QuickAdd file*

3. Press Quick Setting tile icon
4. Submit text

5. (post-processing on text)
6. (write text)


QuickAdd input dialog:
![[Screenshot_20220129-124623_ObsidianCompanion.png]]

outputted in obsidian with post-processing:
![[Screenshot_20220129-124148_Obsidian.png]]



### Configuring  post-processing

You can use variables and markdown in these.


Prepend default example:

![[Screenshot_20220129-054147_ObsidianCompanion 1.png]]

Append default example:

![[Screenshot_20220129-054153_ObsidianCompanion 1.png]]

outputted in obsidian with post-processing:

![[Screenshot_20220129-124148_Obsidian.png]]
