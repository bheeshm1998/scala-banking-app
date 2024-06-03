(function(window) {
    window["env"] = window["env"] || {};
    console.log("Inside the env.js file");
    // Environment variables
    window["env"]["usersAPIEndpoint"] = "${USERS_API_ENDPOINT}";
    window["env"]["accountAPIEndpoint"] = "${ACCOUNT_API_ENDPOINT}";
    window["env"]["transactionAPIEndpoint"] = "${TRANSACTION_API_ENDPOINT}";
  })(this);
  