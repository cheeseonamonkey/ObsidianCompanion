## QuickAdd Usage

Gif usage example
-GIF EXAMPLE-

1. **Enable required permissions**
2. **Set QuickAdd file**
3. **Activate the Quick Settings tile by dragging it in the notification tray**

4. Press Quick Setting tile icon
5. Submit text

6. *(post-processing is done on text)*
7. *(appends text to the end of the picked file)*


QuickAdd input dialog:
![6_dialog](https://user-images.githubusercontent.com/54555500/153515167-1b32df24-4e56-4f22-a175-98945fc8376a.png)

outputted in obsidian with post-processing:
![7_added](https://user-images.githubusercontent.com/54555500/153515171-d0d30bdf-32b7-45a6-9ce8-05913950e5e8.png)



### Configuring  post-processing

You can use variables and markdown in these.


Prepend default example:
![4_prepend](https://user-images.githubusercontent.com/54555500/153515139-27de8a6c-b473-4be6-8fbd-4287976dcaaf.png)

Append default example:

![5_append](https://user-images.githubusercontent.com/54555500/153515142-d90fa6eb-f00a-4892-9c9f-dfe2c25a4106.png)

outputted in obsidian with post-processing:

![7_added](https://user-images.githubusercontent.com/54555500/153515229-fe43627e-18d7-4a4b-a1d3-e13c22ea2683.png)




# ObsidianCompanion
Android companion app to Obsidian.MD

## Settings Screen

Settings screen

![1_settingscreen](https://user-images.githubusercontent.com/54555500/153515117-d9ef9b46-fc26-4888-b5ca-a45b33e443c3.png)



### Request permissions
You will be brought to file permissions in your settings

![2_fileaccess](https://user-images.githubusercontent.com/54555500/153515127-e302aecd-bc9f-4528-b338-29eb7e91798e.png)

Enable ObsidianCompanion on this setting page.

![3_fileaccess2](https://user-images.githubusercontent.com/54555500/153515132-9066f9cc-8dee-4cd4-b25d-f8f1a435b949.png)

You will also be asked for file write acess.


### Set default QuickAdd file
Select the default target file in vault for QuickAdd in user file explorer.


### Post-processing

Used to template your QuickAdd data.

Add new lines, variables, and markdown text.

#### prepend
This is added *before* the QuickAdd data .

![4_prepend](https://user-images.githubusercontent.com/54555500/153515139-27de8a6c-b473-4be6-8fbd-4287976dcaaf.png)

#### append
This is added *after* the QuickAdd data .

![5_append](https://user-images.githubusercontent.com/54555500/153515142-d90fa6eb-f00a-4892-9c9f-dfe2c25a4106.png)

### Variables

Variables will be replaced in post-processing:
- `[!empty]`
- `[!time]`
- `[!date]`




