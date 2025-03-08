package uk.gov.dwp.uc.pairtest;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceImplTest extends TestCase {

    @Mock
    private TicketPaymentService paymentService;
    @Mock
    private SeatReservationService seatReservationService;
    @InjectMocks
    private TicketServiceImpl ticketServiceImpl;

    @Before
     public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void checkAccountIDIsInValid()  {
        TicketTypeRequest req= new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 20);
     try{
         ticketServiceImpl.purchaseTickets(0L,req);
     }catch (InvalidPurchaseException e){
         assertEquals("Invalid Account ID", e.getMessage());
     }
    }

    @Test
    public void checkTotalTicketsCountValid()  {
        TicketTypeRequest req= new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 25);
        try{
            ticketServiceImpl.purchaseTickets(1L,req);
        }catch (InvalidPurchaseException e){
            assertEquals("Invalid ticket request: Must purchase between 1 and 25 tickets.", e.getMessage());
        }
    }

    @Test
    public void checkTotalTicketsCountInValid()  {
        TicketTypeRequest req= new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 0);
        try{
            ticketServiceImpl.purchaseTickets(1L,req);
        }catch (InvalidPurchaseException e){
            assertEquals("Invalid ticket request: Must purchase between 1 and 25 tickets.", e.getMessage());
        }
    }

    @Test
    public void checkTotalTicketsCountInValid1()  {
        TicketTypeRequest req= new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 26);
        try{
            ticketServiceImpl.purchaseTickets(1L,req);
        }catch (InvalidPurchaseException e){
            assertEquals("Invalid ticket request: Must purchase between 1 and 25 tickets.", e.getMessage());
        }
    }

    @Test
    public void checkValidAccountIdAndTicketType()  {
        TicketTypeRequest req = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest req1 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 4);
        ticketServiceImpl.purchaseTickets(1L,req,req1);
    }

    @Test
    public void checkValidTicketTypeChildOnly()  {
        TicketTypeRequest req = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2);
        try{
            ticketServiceImpl.purchaseTickets(1L,req);
        }catch (InvalidPurchaseException e){
            assertEquals("Child and Infant tickets cannot be purchased without purchasing an Adult ticket.", e.getMessage());
        }
    }
    @Test
    public void checkValidTicketTypeInfantOnly()  {
        TicketTypeRequest req = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2);
        try{
            ticketServiceImpl.purchaseTickets(1L,req);
        }catch (InvalidPurchaseException e){
            assertEquals("Child and Infant tickets cannot be purchased without purchasing an Adult ticket.", e.getMessage());
        }
    }
    @Test
    public void checkValidTicketTypeAdultOnly()  {
        TicketTypeRequest req = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
            ticketServiceImpl.purchaseTickets(1L,req);
    }

  /*   @Test
    public void checkPaymentAndSeatAllocation() {
        TicketTypeRequest t1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        TicketTypeRequest t2 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2);
        TicketTypeRequest t3 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2);

        ticketServiceImpl.purchaseTickets(1L, t1, t2,t3);
        Mockito.verify(paymentService, times(1)).makePayment(eq(1L), eq(55));
        Mockito.verify(seatReservationService,times(1)).reserveSeat(eq(1L), eq(3));

    }*/
}