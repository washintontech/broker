# 🏦 Stock Broker System

## 📌 Overview

A high-performance, stockbroker system designed to handle order ingestion, submitting to Exchange and keeping trade records.

---

## 🎯 Problem Statement

Design a scalable system capable of:

* Consume end user trade orders on REST api.
* Save the user requested order.
* Send FIX message to Exchange.
* Respond back to user on REST call.

* Consume Exchange FIX messages (Execution Report) on FIX protocol.
* Save Execution reports against requested order by end user.

---

## 🏗️ Architecture

```
End User
   ↓
Broker  
   ↓ 
Exchange          

    
```

---
## ⚙️ Tech Stack

* Java 21
* Spring Boot
* In-memory structures
* FIX Protocol (QuickFIX/J)

---

## 🛠️ How to Run

```bash
# Example
mvn clean install
```

---

## 🔮 Future Improvements

* Sending post trade response to user.
* Support for APIs : trade history, Pending trades.
* Support for APIs : Price depth of any stock on bid/ask side.
* Reconciliation of trades and orders.
* Fault tolerance


