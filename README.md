# OnionTool
"あると便利かも" 管理者向けBukkitプラグイン

![image](https://user-images.githubusercontent.com/62508399/218626042-15bbdc78-cf30-46d9-9ac3-e9164868600b.png)

## 主な機能
- HP全回復 (WorldGuard /heal のような)
- 無敵モード (WorldGuard /god のような)
- 暗視ポーション効果の切り替え
- 被ダメージを表示する機能
- 受けたダメージを攻撃者に反射させる機能
- 手に持ったアイテムの耐久値を全回復させる
- 指定したアイテムにコマンドを設定し、アイテムをクリックするだけで実行できる機能

※ 各種モードの状態はプラグインが有効の間まで維持します。


## 前提
- Spigot 1.13 以上 (またはその派生)


## コマンドと権限
- クイック画面を開きます - `/oniontoolgui`, `/o/`
> 権限: `oniontool.command.gui` (default: OP)
<br>

- プレイヤーのHPを回復させます - `/onionheal`, `/o/heal`
> ※ 複数の対象やエンティティにも使用できます<br>
> 引数: `/o/heal [player/uuid/selector]`<br>
> 権限: `oniontool.command.heal` (default: OP)
<br>

- プレイヤーの無敵モードを設定します - `/oniongod`, `/o/god`
> 引数: `/o/god [player/uuid/selector]`<br>
> 権限: `oniontool.command.god` (default: OP)
<br>

- プレイヤーの無敵モードを解除します - `/onionungod`, `/o/ungod`
> 引数: `/o/ungod [player/uuid/selector]`<br>
> 権限: `oniontool.command.god` (default: OP)
<br>

- 暗視効果を切り替えます - `/onionbright`, `/o/bright`
> 引数: `/o/bright [player/uuid/selector]`<br>
> 権限: `oniontool.command.bright` (default: OP)
<br>

- 被ダメージ表示機能を切り替えます - `/onionviewdamage`, `/o/viewdamage`
> 権限: `oniontool.command.viewdamage` (default: OP)
<br>

- ダメージ反射機能を切り替えます - `/onionreflectdamage`, `/o/reflectdamage`
> 権限: `oniontool.command.reflectdamage` (default: OP)
<br>

- 手持ちアイテムの耐久値を回復します - `/onionrepairitem`, `/o/repairitem`
> 権限: `oniontool.command.repairitem` (default: OP)
<br>

- 手持ちアイテムに実行コマンドを追加します - `/onionaddcmditem`, `/o/addcmditem`
> 引数: `/o/addcmditem (command)`<br>
> 権限: `oniontool.command.cmditem` (default: OP)
<br>

- 手持ちアイテムから実行コマンドを削除します - `/onionremovecmditem`, `/o/removecmditem`
> 引数: `/o/removecmditem (number)`<br>
> 権限: `oniontool.command.cmditem` (default: OP)
<br>

- 手持ちアイテムの実行コマンドを表示します - `/onionlistcmditem`, `/o/listcmditem`
> 権限: `oniontool.command.cmditem` (default: OP)
