package ru.bank.consultant.service;

import ru.bank.consultant.dto.CreditFilterDto;
import ru.bank.consultant.entity.Bank;
import ru.bank.consultant.entity.LoanRequest;
import ru.bank.consultant.entity.RequestStatus;
import ru.bank.consultant.entity.User;
import ru.bank.consultant.repository.BankRepository;
import ru.bank.consultant.repository.LoanRequestRepository;
import ru.bank.consultant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanRequestService {

    @Autowired
    private LoanRequestRepository loanRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankRepository bankRepository;


    // старый метод сохранения
    public void saveRequest(CreditFilterDto filter, BigDecimal monthlyPayment) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) return;

        LoanRequest request = new LoanRequest();
        request.setUser(user);
        request.setAmount(filter.getAmount());
        request.setTermMonths(filter.getTermMonths());
        request.setPurpose(filter.getCreditType());
        request.setIncome(filter.getIncome());
        request.setEmploymentType(filter.getEmploymentType());
        request.setCreditHistory(filter.getCreditHistory());
        request.setMonthlyPayment(monthlyPayment);
        request.setStatus(RequestStatus.in_progress);
        request.setCreatedAt(LocalDateTime.now());

        loanRequestRepository.save(request);
    }

    //сохраняет и возвращает заявку
    public LoanRequest saveRequestAndReturn(CreditFilterDto filter, BigDecimal monthlyPayment) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) return null;

        LoanRequest request = new LoanRequest();
        request.setUser(user);
        request.setAmount(filter.getAmount());
        request.setTermMonths(filter.getTermMonths());
        request.setPurpose(filter.getCreditType());
        request.setIncome(filter.getIncome());
        request.setEmploymentType(filter.getEmploymentType());
        request.setCreditHistory(filter.getCreditHistory());
        request.setMonthlyPayment(monthlyPayment);
        request.setStatus(RequestStatus.in_progress);
        request.setCreatedAt(LocalDateTime.now());

        return loanRequestRepository.save(request);
    }

    public void updateSelectedBank(Long requestId, Long bankId) {
        LoanRequest request = loanRequestRepository.findById(requestId).orElse(null);
        if (request != null) {
            Bank bank = bankRepository.findById(bankId).orElse(null);
            request.setSelectedBank(bank);
            request.setStatus(RequestStatus.bank_selected);
            loanRequestRepository.save(request);
        }
    }

    public List<LoanRequest> getUserRequests() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return List.of();
        return loanRequestRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }
}