package com.example.expense_services.controller;

import com.example.expense_services.model.vo.ExpenseVo;
import com.example.expense_services.model.vo.NonGroupExpenseVo;
import com.example.expense_services.model.vo.Response;
import com.example.expense_services.model.vo.UserExpenseSummary;
import com.example.expense_services.service.ExpenseService;
import com.example.expense_services.service.NonGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/httpmethod")
public class HttpMethodController {
    @Autowired
    ExpenseService expenseBean;
    @Autowired
    NonGroupService nonGroupBean;
    @PostMapping("/add/expense")
    public Response addNewExpense(@RequestBody ExpenseVo expenseVo){
        return expenseBean.addExpense(expenseVo);
    }
    @PutMapping("/payment/{expenseId}/{userId}")
    public Response userPaymentRequest(@PathVariable String expenseId , @PathVariable long userId){
        return expenseBean.userPayment(expenseId , userId);
    }
    @GetMapping("/expense-list/{groupId}")
    public List<ExpenseVo> groupExpenseList(@PathVariable long groupId){
        return expenseBean.expenseList(groupId);
    }
    @GetMapping("/filter-amount/{userId}")
    public UserExpenseSummary getUserExpenseSummaryRequest(@PathVariable long userId){
        return expenseBean.getUserExpenseSummary(userId);
    }
    @GetMapping("/filter-group-category")
    public List<ExpenseVo> filterGroupCategory(
            @RequestParam(name = "groupId") long groupId,
            @RequestParam(name = "category") String category){
        if(category.equals("All"))
            return groupExpenseList(groupId);
        return expenseBean.filterByGroupIdAndCategory(groupId , category);
    }
    @GetMapping("/filter/user-category/{userId}")
    public List<UserExpenseSummary> filterUserAllCategory(@PathVariable long userId){
        return expenseBean.filterAllCategory(userId);
    }
    @PutMapping("/settlement/{userId}/{groupId}")
    public Response userGroupSettlement(@PathVariable long userId , @PathVariable long groupId){
        return expenseBean.groupSettlement(userId,groupId);
    }
    @GetMapping("/payment-pending/{userId}/{groupId}")
    public String paymentsPending(@PathVariable long userId , @PathVariable long groupId){
        return expenseBean.pendingPaymentCheck(userId , groupId);
    }
    @GetMapping("/category-list")
    public Response categoryListRequest(){
        return expenseBean.categoryList();
    }
    @GetMapping("/non-group-expenses/{createdUser}/{receiverUser}")
    public List<NonGroupExpenseVo> getNonGroupExpenseList(@PathVariable long createdUser, @PathVariable long receiverUser){
        return nonGroupBean.nonGroupPaymentsList(createdUser,receiverUser);
    }
    @PostMapping("/add/non-group-expense")
    public Response nonGroupExpense(@RequestBody NonGroupExpenseVo nonGroupExpenseVo) {
        return nonGroupBean.addNonGroupExpense(nonGroupExpenseVo);
    }
    @PutMapping("/non-group-pay/{expenseId}/{memberId}")
    public Response nonGroupExpensePay(@PathVariable String expenseId , @PathVariable long memberId){
        return nonGroupBean.payNonGroupExpense(expenseId , memberId);
    }
    @GetMapping("/user-payment-pending/{userId}/{memberId}")
    public String UserPaymentsPending(@PathVariable long userId , @PathVariable long memberId){
        return nonGroupBean.pendingUserPaymentCheck(userId , memberId);
    }
}
