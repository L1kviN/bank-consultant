package ru.bank.consultant.repository;

import ru.bank.consultant.entity.CreditOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CreditOfferRepository extends JpaRepository<CreditOffer, Long> {

    List<CreditOffer> findByBankId(Long bankId);

    Optional<CreditOffer> findByBankIdAndCreditType(Long bankId, String creditType);
}