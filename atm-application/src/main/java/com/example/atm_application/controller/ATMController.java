package com.example.atm_application.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.atm.Customer;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ATMController {
    private final Map<Integer, Customer> accounts = new HashMap<>();
    private Integer currentNum = null;

    public ATMController() {
        accounts.put(123456, new Customer("Asif", 0.0f,7675));
        accounts.put(654321, new Customer("Hannah", 0.0f,3333));
        accounts.put(111111, new Customer("John", 0.0f,1238));
    }

    @GetMapping("/")
    public String showPinForm() {
        return "pin";
    }
    
    @GetMapping("/createaccount")
    public String showCreateAccountForm(Model model) {
        return "createaccount"; // Renders the createaccount.html page

    }
    
    @PostMapping("/saveaccount")
    public String saveAccount(@RequestParam String name, @RequestParam int num, @RequestParam int pin, Model model) {
        // Check if the PIN is already in use
        if (accounts.containsKey(num)) {
            model.addAttribute("error", "Account Number already exists, please choose another one.");
            return "createaccount";
        }
        
        // Create a new account and add it to the accounts map
        Customer newCustomer = new Customer(name, 0.0f,pin);  // New customer starts with a balance of 0
        accounts.put(num, newCustomer);

        model.addAttribute("message", "Account successfully created!");
        return "redirect:/";  // Redirect back to the pin entry page for login
    }
    
    @GetMapping("/menu")
    public String showMenu(Model model) {
        if (currentNum != null) {
            model.addAttribute("customer", accounts.get(currentNum));
            return "menu";
        }
        return "redirect:/";
    }

    
    
    @PostMapping("/checkpin")
    public String checkPin(@RequestParam int num, @RequestParam int pin, Model model) {
        // Retrieve customer by customer number directly (no loop required)
        Customer customer = accounts.get(num);

        if (customer != null) {
            // Check if the entered PIN matches the stored PIN
            if (customer.getPin() == pin) {
                currentNum = num;  // Track the current customer number
                model.addAttribute("customer", customer);
                return "menu";  // Proceed to the menu if PIN is correct
            } else {
                model.addAttribute("error", "Invalid PIN.");
                return "pin";  // Return to the PIN page with an error message
            }
        } else {
            // If customer number does not exist in the map
            model.addAttribute("error", "Invalid account number.");
            return "pin";  // Return to the PIN page with an error message
        }
    }



    		

    @PostMapping("/deposit")
    public String deposit(@RequestParam float amount, Model model) {
        if (currentNum != null) {
            Customer customer = accounts.get(currentNum);
            customer.setBalance(customer.getBalance() + amount);
            customer.addTransaction("Deposited: " + amount);
            model.addAttribute("deposit" , "Successfully Deposited the amount !");
            model.addAttribute("customer", customer);
            return "menu";
        }
        return "redirect:/";
    }

    
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam float amount, Model model) {
        if (currentNum != null) {
            Customer customer = accounts.get(currentNum);
            if (amount > customer.getBalance()) {
                model.addAttribute("error", "Insufficient balance!");
            } else {
                customer.setBalance(customer.getBalance() - amount);
                customer.addTransaction("Withdrew: " + amount);
                model.addAttribute("success" , "Withdrawal Successful !");
            }
            model.addAttribute("customer", customer);
           return "menu";
        }
        return "redirect:/";
    }

    @GetMapping("/transactions")
    public String viewTransactions(Model model) {
        if (currentNum != null) {
            Customer customer = accounts.get(currentNum);
            model.addAttribute("transactions", customer.getTransactionHistory());
            return "transactions";
        }
        return "redirect:/";
    }
    
    @GetMapping("/checkbalance")
    public String viewBalance(Model model) {
        if (currentNum != null) {
            Customer customer = accounts.get(currentNum);
            model.addAttribute("customer", customer);  // Add customer object to model
            return "checkbalance";  // Return to the viewbalance.html page
        }
        return "redirect:/";  // Redirect if no customer is logged in
    }




    @PostMapping("/logout")
    public String logout() {
        currentNum = null;
        return "redirect:/";
    }
}
