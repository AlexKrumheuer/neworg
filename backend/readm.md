# Backend DOCS

Explaining later...
---

## Entities

This section refers about Database's tables and their representation in JPA

### 1. Asset
It represents a negotiable financial asset.

* **File:** `Asset.java`
* **Campos Principais:**
    * `id`: `Long` (PK, Auto-increment)
    * `ticker`: `String` (Unique, Not null. Ex: "PETR4")
    * `name`: `String` (Company's name)
    * `type`: `AssetType` (Enum)

#### Enum: AssetType
It defines asset's category.
* Possible Values: `STOCK_US`, `STOCK_BR`, `ACAO`, `TESOURO`, `FII`, `CRYPTO`, `CDB`.

### 2. Transactions
It represents the register of a buy or sell operation.
* **File** `Transactions.java`
* **Main Rows:**
    * `id`: `Long` (PK, Auto-increment)
    * `asset`: `Asset` (ManyToOne relationship)
    * `quantity`: `BigDecimal` (Negotiated quantity with precision of 19,4)
    * `price`: `BigDecimal` (Unit price, precision of 19,4)
    * `transactionDate`: `LocalDateTime` (Transaction's date and time)
    * `type`: `TransactionType` (Enum)

#### Enum: TransactionType
It defines the type of transaction:
* Values: `BUY` (Buy), `SELL` (Sell).

---

## 🧠 Businnes rules

The application main logic is into `@Service` files.

### 1. AssetService
It refers to the logic of Asset's CRUD

* **Ticker's unicity:** Before being created (`createAsset`) or edited (`editAsset`), it's verified if it already exists another asset with the same ticker using the method `existsByTickerIgnoreCase`. If it already exists, it throws `IllegalArgumentException`.
* **Validation:** It searchs the asset by its ID, if not found, then it throws an exception of not found.

### 2. TransactionService
It manages the register of financial operations.

* **Link:** Enter DTO (`TransactionDTO`) is given only`assetId`. The service searchs for the full entity `Asset` in Database before it saves.
* **Validation**
    * In method `saveTransaction`, if type is `SELL`, system will verify if the asset's balance in database is greater or equal.
    * It uses personal query  `getBalanceByAssetId`, that sums the purchases and subtracts the sells.
    * If `Sell quantity > Actual balance`, the operation is blocked with the exception `IllegalArgumentException`.

### 3. PortfolioService
It's in order to consolidate the wallet in memory.

* **Aggregation logic:**
    1.  Find all user's transaction(`findAll`).
    2.  It groups all transactions by ticker into `Map<String, List<Transactions>>`.
    3.  Iterate over each group calculating its balance: `Soma (BUY) - Soma (SELL)`.
    4.  It generates a list of `PortfolioItemDTO` containing only assets with positive balance (`> 0`).
* **Goal:** It's used to turn the transaction's history into a "photo" of its actual state to then display it in frotend.

---

## 📡 API Endpoints (Controllers)

### 📦 Assets (`/api/assets`)
Controller: `AssetController`

| Method | Route | Description |
| :--- | :--- | :--- |
| `GET` | `/` | It returns all assets that the user has bought |
| `GET` | `/{id}` | It returns a specific asset by its id. |
| `POST` | `/` | It register a new asset that the user bought. It uses `AssetDTO` to make data validation  (ticker, name, type). |
| `PUT` | `/{id}` | This is for the system manager. |
| `DELETE` | `/{id}` | This is also for the system manager. |

### 💰 Transactions (`/api/transactions`)
Controller: `TransactionController`

| Method | Route | Description |
| :--- | :--- | :--- |
| `GET` | `/` | It returns all the user's transactions. |
| `GET` | `/{id}` | It searches for a specific transaction. |
| `GET` | `/asset/{id}` | It returns all the transactions of a specific Asset |
| `POST` | `/` | It register a new transaction. It validates the balance in case of SELL .`TransactionDTO`. |
| `PUT` | `/{id}` | It Edits an existing transaction |
| `DELETE` | `/{id}` | It removes an existing transaction |

### 📊 Portfolio (`/api/portfolio`)
Controller: `PortfolioController`

| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/` | Retorna a posição consolidada da carteira (Lista de ativos com saldo > 0). |

---

## 🗂 Folder Structure of backend

```text
src/main/java/com/neworg/neworg/
├── asset/           # Asset's Entity, Repository, Service and Controller
├── transaction/     # Transaction's Entity, Repository, Service and Controller
├── portfolio/       # Consolidation Logic (Service, Controller e DTO)
└── config/          # I should define it better later