import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {HttpClient, HttpClientModule} from "@angular/common/http";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ReactiveFormsModule, CommonModule, HttpClientModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  username: String = "-"
  accountBalance: Number = 0
  accountType:String = ""
  userRole: String = "Customer"
  simpleForm!: FormGroup;
  loginForm!: FormGroup;

  transferAmountForm: FormControl = new FormControl("");
  transferToAccountNumberForm: FormControl = new FormControl("");
  withdrawForm: FormControl = new FormControl("");
  depositForm: FormControl = new FormControl("");

  currentAccountId: any = 3;

  showLoginScreen = true;

  constructor(private fb: FormBuilder, private http: HttpClient) { }

  ngOnInit(): void {
    this.simpleForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      phoneNumber: ['', Validators.required],
      role: ['', Validators.required]
    });

    this.loginForm = this.fb.group({
      userId: ['', [Validators.required, Validators.email]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });

    // this.getAccountDetails(this.currentAccountId);
    this.makeApiCall();
  }

  env: any = window['env'];

  get usersAPIEndpoint(): string {
    return this.env.usersAPIEndpoint || 'http://localhost:9001/users';  // default value if not set
  }

  get accountAPIEndpoint(): string {
    return this.env.accountAPIEndpoint || 'http://localhost:9002/users';  // default value if not set
  }

  get transactionAPIEndpoint(): string {
    return this.env.transactionAPIEndpoint || 'http://localhost:9003/users';  // default value if not set
  }

  makeApiCall(){
    this.http.get("http://35.200.157.92:9007").subscribe((res: any) => console.log("PURVI" , res))
  }

  onSubmit(): void {

    console.log("validity status ", this.simpleForm.valid)
      console.log("Form Value "+ this.simpleForm.value);
      this.http.post(`${this.usersAPIEndpoint}/add`, this.simpleForm.value)
        .subscribe((response: any) => {
          console.log('Form Submitted!', this.simpleForm.value);
          console.log('API response', response);

          this.http.post(`${this.accountAPIEndpoint}/add`, {
            "accountType": response.role,
            "username": response.email
          })
            .subscribe((res: any) => {
              this.showLoginScreen = false;
              this.username = res.userId
              this.currentAccountId = res.id
              this.accountType = res.accountType
              this.accountBalance = res.balance
            }, error => {
              console.error('Account creation API error', error);
            });

        }, error => {
          console.error('Add user API error', error);
        });
  }

  onClickDepositConfirm(): void{
    console.log(this.depositForm.value);
    this.depositMoney(this.currentAccountId, this.depositForm.value);
    // this.getAccountDetails(this.currentAccountId);
  }

  onClickWithdrawConfirm(): void {
    console.log(this.withdrawForm.value);
    this.withdrawMoney(this.currentAccountId, this.withdrawForm.value);
  }

  onClickTransferConfirm(): void {
    console.log(this.transferToAccountNumberForm.value);
    console.log(this.transferAmountForm.value);
    this.transferMoney(this.transferToAccountNumberForm.value, this.transferAmountForm.value)
  }

  depositMoney(accountId: any, amount: Number) {
    this.http.post(`${this.transactionAPIEndpoint}/deposit`, {
      "accountId": accountId.toString(),
      "amount": amount.toString()
    })
      .subscribe(response => {
        console.log('API response', response);
        this.getAccountDetails(this.currentAccountId);
      }, error => {
        console.error('API error', error);
      });
  }

  withdrawMoney(accountId: any, amount: Number) {
    this.http.post(`${this.transactionAPIEndpoint}/withdraw`, {
      "accountId": accountId.toString(),
      "amount": amount.toString()
    })
      .subscribe(response => {
        console.log('API response', response);
        this.getAccountDetails(this.currentAccountId);
      }, error => {
        console.error('API error', error);
      });
  }

  transferMoney(accountId: any, amount: Number) {
    this.http.post(`${this.transactionAPIEndpoint}/transfer`, {
      "fromAccountId": this.currentAccountId.toString(),
      "toAccountId": accountId.toString(),
      "amount": amount.toString()
    })
      .subscribe(response => {
        console.log('API response for the transfer request', response);
        this.getAccountDetails(this.currentAccountId);
      }, error => {
        console.error('API error', error);
      });
  }

  getAccountDetails(accountId: String): void {
    this.http.get(`${this.accountAPIEndpoint}/${accountId}`)
      .subscribe((response: any) => {
        this.userRole = response.role;
        this.accountBalance = response.balance;
        this.username = response.userId;
        this.accountType = response.accountType;
        console.log('API response', response);
        this.showLoginScreen = false;
      }, error => {
        console.error('API error', error);
      });
  }

  onClickLogout(){
    this.showLoginScreen = true;
  }

  onSubmitLoginForm(){
    this.http.post(`${this.usersAPIEndpoint}/user/login`, this.loginForm.value)
      .subscribe((response: any) => {
        console.log('Form Submitted!', this.simpleForm.value);
        console.log('API response', response);
        this.http.get(`${this.accountAPIEndpoint}user/${this.loginForm.controls["email"].value}`).subscribe((res: any) =>{
          this.currentAccountId = res.id;
          this.getAccountDetails(this.currentAccountId);
        })
      }, error => {
        console.error('Add user API error', error);
      });
  }

}
