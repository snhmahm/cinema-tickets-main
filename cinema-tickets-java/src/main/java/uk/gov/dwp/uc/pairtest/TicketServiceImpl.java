package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */
    private final TicketPaymentService paymentService;
    private final SeatReservationService seatReservationService;

    public TicketServiceImpl(TicketPaymentService paymentService, SeatReservationService seatReservationService) {
        this.paymentService = paymentService;
        this.seatReservationService = seatReservationService;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException("Invalid Account ID");
        }

        int child = 0, infant = 0, adult = 0;

        for (TicketTypeRequest req : ticketTypeRequests) {
            if (req.getNoOfTickets() > 0) {
                switch (req.getTicketType()) {
                    case INFANT:
                        infant += req.getNoOfTickets();
                        break;
                    case CHILD:
                        child += req.getNoOfTickets();
                        break;
                    case ADULT:
                        adult += req.getNoOfTickets();
                        break;
                }
            }

        }
        int totaltickets = infant + child + adult;
        if (totaltickets == 0 || totaltickets > 25) {
            throw new InvalidPurchaseException("Invalid ticket request: Must purchase between 1 and 25 tickets.");
        }
        if (adult == 0 && (child > 0 || infant > 0)) {
            throw new InvalidPurchaseException("Child and Infant tickets cannot be purchased without purchasing an Adult ticket.");
        }
        int amount = (adult * 25) + (child * 15);
        int seats = adult + child;

        paymentService.makePayment(accountId, amount);

        seatReservationService.reserveSeat(accountId, seats);


    }
}
