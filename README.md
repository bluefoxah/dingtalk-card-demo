# 使用方法
## 运行代码
### step1 导入工程
### step2 导入模板
请在钉钉互动卡片平台，新建一张「标准卡片」，然后导入[demo_card_template.json](/demo_card_template.json),后点击保存，发布，然后获取到模板id
### step3 修改application.properties
修改文件<>部分为你自己正确的值
```properties
app.key=
app.secret=

group.owner=
group.members=
group.robotcode=

card.templateid=
```
### step4 启动工程

## 测试方法
### 1 新建群
#### 1.1 普通建群
```shell
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:8080/test/createGroup -d '{}'
```
#### 1.2 通过群模板创建群
```shell
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:8080/test/createGroupByTemplate -d '{}'
```
### 2 往新建群里添加机器人
### 3 创建卡片
```shell
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:8080/test/createCard -d '{}'
```
### 4 投放卡片到群吊顶
```shell
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:8080/test/deliverCardToTop -d '{}'
```
### 5 投放卡片到群消息
```shell
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:8080/test/deliverCardToGroup -d '{}'
```
### 6 更新卡片
```shell
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:8080/test/updateCard -d '{}'
```