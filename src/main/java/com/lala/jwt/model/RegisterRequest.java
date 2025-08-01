package com.lala.jwt.model;

public class RegisterRequest {
        private String email;
        private String password;
        private String role;
        private Double balance;

        public Double getBalance() {
                return balance;
        }

        public void setBalance(Double balance) {
                this.balance = balance;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }
        public String getRole() {
                return role;
        }
        public void setRole(String role) {
                this.role = role;
        }

}
