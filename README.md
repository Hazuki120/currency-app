
# 通貨変換アプリ（Spring Boot + MySQL + Docker）

外部 API から取得した通貨レートをユーザーごとに保存し、最新レートを使って金額変換ができる Web アプリです。  
Spring Boot を用いたバックエンド開発に加え、認証・DB設計・API連携・Docker による環境構築までを一貫して実装しています。
---

## 開発背景
Spring Boot の理解を深めるため、以下の要素を含む実践的な Web アプリを制作しました。

 - 認証機能 (Spring Security）
 - DB 永続化（Spring Data JPA）
 - 外部 API 連携
 - ユーザ単位のデータ分離
 - Docker によるコンテナ環境構築
 
 単なる CRUD アプリではなく、実務に近い構成を意識して設計しています。

## 主な機能
 - ユーザー登録・ログイン（Spring Security）
 - 外部 API から通貨レート取得
 - レートの自動保存（ユーザーごと）
 - 1時間以内のﾚｰﾄﾊｻｓsあ
 - 金額変換（例：USD → JPY）
 - ユーザーごとのレート履歴表示
 ---
 
## 使用技術

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* MySQL 8.0（Docker）
* Thymeleaf
* Docker
---

## アプリ全体構成図

写真

### ER 図

写真

## 画面キャプチャ

### ログイン画面

写真

### 通貨変換画面
写真

### レート履歴画面

写真

---

## セットアップ手順

### 1. リポジトリをクローン   
```bash
git clone https://github.com/Hazuki120/exchange.git
```

### 2. Docker で MySQL を起動
#### 起動
```bash
docker compose up -d
```
#### 停止
```bash
docker compose down
```

### 3. `.env` の設定（Spring Boot 用）
Spring Boot は `application.properties` ではなく `.env` を読み込みます。  
例：

```env
DB_URL=jdbc:mysql://localhost:3306/currency?useSSL=false&serverTimezone=Asia/Tokyo
DB_USER=appuser
DB_PASSWORD=apppass
API_KEY=xxxxx
```
⚠ Windows の環境変数に同名キーがあると `.env` が無視されるので注意。  

### 4. アプリを起動
```bash
./mvnw spring-boot:run
```
または IDE から `ExchangeApplication` を実行。

---

## API エンドポイント一覧
| メソッド | パス | 説明 |
|----------|------|------|
| GET | /exchange | 通貨変換フォーム |
| GET | /exchange/result | 変換結果表示 |
| GET | /rates | ユーザーのレート履歴 |
| GET | /latest | 最新レート取得 |
| GET | /save | レート保存（内部用） |

## 今後の課題
 - API レート制限対策（キャッシュ強化）
 - グラフ表示（レート推移）
 - UI/UX 改善（Bootstrap 導入）

 
 
 