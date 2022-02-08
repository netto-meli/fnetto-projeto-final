package com.mercadolibre.w4g9projetofinal.test.integration;

import com.mercadolibre.w4g9projetofinal.entity.*;
import com.mercadolibre.w4g9projetofinal.entity.enums.AdvertiseStatus;
import com.mercadolibre.w4g9projetofinal.entity.enums.RefrigerationType;
import com.mercadolibre.w4g9projetofinal.entity.enums.RepresentativeJob;
import com.mercadolibre.w4g9projetofinal.repository.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WithMockUser(username = "admin", roles = {"ADMIN"})
public class InboundOrderContorllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BCryptPasswordEncoder pe;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RepresentativeRepository representativeRepository;
    @Autowired
    private AdvertiseRepository advertiseRepository;
    @Autowired
    private WarehouseRepository warehouseRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private BuyerRepository buyerRepository;
    @Autowired
    private InboundOrderRepository inboundOrderRepository;

    @Test
    @Order(1)
    public void populateDB() {

        Warehouse warehouse = new Warehouse(null, "k", "l");
        warehouse = warehouseRepository.save(warehouse);

        Buyer buyer = new Buyer(null, "userComprador",
                "Comprador nome", "email1@hotkkmail.com", pe.encode("123776456"), "Endereco");
        buyer = buyerRepository.save(buyer);

        Seller seller = new Seller(null, "userSeller",
                "vendedor nome", "email1@hotmail.com", pe.encode("123456"), null);
        seller = sellerRepository.save(seller);

        Representative representative = new Representative(null, "userRepresentative",
                "Representante nome", "em1@gmail.com", pe.encode("151515"),
                RepresentativeJob.LIDER, warehouse);
        representative = representativeRepository.save(representative);

        Product product1 = new Product(null, "produto1", "desc produto 1",
                10F, 20F, RefrigerationType.FRESH);
        Product product2 = new Product(null, "produto2", "desc produto 2",
                10F, 20F, RefrigerationType.FRESH);
        product1 = productRepository.save(product1);
        product2 = productRepository.save(product2);

        product1 = productRepository.findById(1L).orElse(null);
        product2 = productRepository.findById(2L).orElse(null);
        Advertise a1 = new Advertise(null, "Anuncio 1", product1, seller, BigDecimal.TEN, AdvertiseStatus.ATIVO, false);
        Advertise a2 = new Advertise(null, "Anuncio 2", product2, seller, BigDecimal.TEN, AdvertiseStatus.ATIVO, true);
        Advertise a3 = new Advertise(null, "Anuncio 3", product1, seller, BigDecimal.TEN, AdvertiseStatus.ATIVO, true);
        Advertise a4 = new Advertise(null, "Anuncio 4", product2, seller, BigDecimal.TEN, AdvertiseStatus.ATIVO, true);
        a1 = advertiseRepository.save(a1);
        a2 = advertiseRepository.save(a2);
        a3 = advertiseRepository.save(a3);
        a4 = advertiseRepository.save(a4);

        Section section1 = new Section(null, warehouse, "Setor1", RefrigerationType.FRESH, 50, 100, 10F, 20F, null);
        section1 = sectionRepository.save(section1);
        Section section2 = new Section(null, warehouse, "Setor2", RefrigerationType.FRESH, 50, 100, 00F, 10F, null);
        section2 = sectionRepository.save(section2);
        Section section3 = new Section(null, warehouse, "Setor3", RefrigerationType.FRESH, 50, 100, -10F, 0F, null);
        section3 = sectionRepository.save(section3);

        LocalDate lc = LocalDate.now();
        LocalDateTime lt = LocalDateTime.now();
        List<Batch> l1 = new ArrayList<>();
        List<Batch> l2 = new ArrayList<>();
        Batch t1 = new Batch(1L, 10, 10, 10F, 10F,
                lc.plusDays(10), lc, lt, a1, null);
        Batch t2 = new Batch(2L, 10, 10, 10F, 10F,
                lc.plusDays(40), lc, lt, a2, null);
        Batch t3 = new Batch(3L, 10, 10, 10F, 10F,
                lc.plusDays(20), lc, lt, a3, null);
        Batch t4 = new Batch(4L, 10, 10, 10F, 10F,
                lc.plusDays(30), lc, lt, a4, null);
        l1.add(t1);
        l1.add(t2);
        l2.add(t3);
        l2.add(t4);
        InboundOrder i1 = new InboundOrder(1L, lc, seller, representative, l1, section1);
        InboundOrder i2 = new InboundOrder(2L, lc, seller, representative, l2, section1);
        i1.setInboundOrderToBatchList();
        i2.setInboundOrderToBatchList();
        inboundOrderRepository.saveAll(Arrays.asList(i1, i2));
    }

    @Test
    @Order(2)
    @WithUserDetails("userRepresentative")
    public void deveInserirUmaOrdemDeEntrada() throws Exception {

        String payLoad = "{\n" +
                "  \"order_number\": 10,\n" +
                "  \"order_date\": \"2022-01-28\",\n" +
                "  \"section\": {\n" +
                "    \"section_code\": \"1\",\n" +
                "    \"warehouse_code\": \"1\"\n" +
                "  },\n" +
                "  \"batch_stock\": [\n" +
                "    {\n" +
                "      \"batch_number\": 10,\n" +
                "      \"advertise_id\": 1,\n" +
                "      \"current_temperature\": 19.0,\n" +
                "      \"minimum_temperature\": 10.0,\n" +
                "      \"initial_quantity\": 2,\n" +
                "      \"current_quantity\": 2,\n" +
                "      \"manufacturing_date\": \"2022-01-28\",\n" +
                "      \"manufacturing_time\": \"2022-01-28T08:28:56.775775\",\n" +
                "      \"due_date\": \"2022-03-28\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/fresh-products/inboundorder/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payLoad))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        System.out.println("\n\nOrdem de entrada alterada:\n" + result.getResponse().getContentAsString() + "\n\n");

    }

    @Test
    @Order(3)
    @WithUserDetails("userRepresentative")
    public void deveAlterarUmaOrdemDeEntrada() throws Exception {

        String payLoad = "{\n" +
                "  \"order_number\": 10,\n" +
                "  \"order_date\": \"2022-01-28\",\n" +
                "  \"section\": {\n" +
                "    \"section_code\": \"1\",\n" +
                "    \"warehouse_code\": \"1\"\n" +
                "  },\n" +
                "  \"batch_stock\": [\n" +
                "    {\n" +
                "      \"batch_number\": 11,\n" +
                "      \"advertise_id\": 1,\n" +
                "      \"current_temperature\": 9.0,\n" +
                "      \"minimum_temperature\": 10.0,\n" +
                "      \"initial_quantity\": 2,\n" +
                "      \"current_quantity\": 2,\n" +
                "      \"manufacturing_date\": \"2022-01-28\",\n" +
                "      \"manufacturing_time\": \"2022-01-28T08:28:56.775775\",\n" +
                "      \"due_date\": \"2022-03-28\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/v1/fresh-products/inboundorder/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payLoad))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        System.out.println("\n\nOrdem de entrada alterada:\n" + result.getResponse().getContentAsString() + "\n\n");

    }

    @Test
    @Order(4)
    public void deveBuscarUmaOrdemDeEntrada() throws Exception {

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/fresh-products/inboundorder/"
                                + 10L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        System.out.println("\n\nOrdem de Entrada:\n" + result.getResponse().getContentAsString() + "\n\n");
    }

    @Test
    @Order(5)
    public void deveBuscarTodasOrdensDeEntrada() throws Exception {

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/fresh-products/inboundorder"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        System.out.println("\n\nLista com todas ordens de entrada:\n" + result.getResponse().getContentAsString() + "\n\n");

    }
}