package com.example.expense_services.service;

import com.example.expense_services.model.vo.NonGroupExpenseVo;
import com.example.expense_services.model.vo.Response;

import java.util.List;

public interface NonGroupService {
    List<NonGroupExpenseVo> nonGroupPaymentsList(long createdUser , long receiverUser);
    Response addNonGroupExpense(NonGroupExpenseVo nonGroupVo);
    Response payNonGroupExpense(String expenseId, long memberId);
    String pendingUserPaymentCheck(long userId , long memberId);
}
