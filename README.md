# exchange_test_task

This application is not supposed to be run in cluster env so it is not used distributed scheduling(quartz) or state synchoniztion via messaging (kafka)

Also, fixer io only have EUR base for free plan that's why we pass it for all currencies ... 

# prerequsite
In order to run up u need: 
- run docker server 
- have acces key for fixer.io 

# how to run
- FIXER_API_KEY=fixer api key ./gradlew  bootRun  (it's not needed to run compose.yml separatly, spring boot does it for u)
- run test ./gradlew clean test --info 

# API: 
# 1. Get available currencies 
GET /api/v1/exchange/currency
 Response: 
[
    "EUR",
    "USD"
]

# 2. Add currency 
POST /api/v1/exchange/currency
Request: 
{
    "currency": "EUR"
}
Response: 200

# 3. Get reates 
Get api/v1/exchange/currency/rates?currency=USD
Response: 
[
    {
        "symbol": "AED",
        "rate": 4.052726
    },
    {
        "symbol": "AFN",
        "rate": 76.929315
    },
    {
        "symbol": "ALL",
        "rate": 99.654561
    },
    {
        "symbol": "AMD",
        "rate": 427.306658
    }
]
