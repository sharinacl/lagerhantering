package se.yrgo.app;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.yrgo.entity.Category;
import se.yrgo.entity.Product;
import se.yrgo.entity.Supplier;
import se.yrgo.exception.ProductNotFoundException;
import se.yrgo.service.CategoryService;
import se.yrgo.service.ProductService;
import se.yrgo.service.SupplierService;
import se.yrgo.service.InventoryTransactionService;

import java.util.Collections;
import java.util.Set;

@Component
public class MockDataInitializer {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryTransactionService inventoryService;

    @PostConstruct
    public void initData() throws ProductNotFoundException {
        // --- Create & save categories ---
        Category tools = new Category("tools", "Devices used for specific tasks, from basic hand tools and power machinery.");
        Category build_material = new Category("build-material", "Raw or processed substances used in construction and repair.");
        Category color = new Category("color", "Products for altering the visual appearance of surfaces, including paints, stains, dyes, and associated applicators.");
        Category clothes = new Category("clothes_protectiveware", "Garments and gear designed to shield the wearer from hazards, ensuring safety during work.");
        Category lights = new Category("lights", "Devices for illumination, ranging from bulbs and fixtures to specialized lighting.");

        categoryService.saveCategory(tools);
        categoryService.saveCategory(build_material);
        categoryService.saveCategory(color);
        categoryService.saveCategory(clothes);
        categoryService.saveCategory(lights);

        // --- Create & save suppliers ---
        Supplier toolsol = new Supplier("Tools Solutions AB", "Anna Andersson",  "anna@techsolutions.se", "08-123456", "Tech Street 1, 111 20 Stockholm");
        Supplier buildmaster = new Supplier("BuildMaster AB", "Lars Lindgren",  "lars@buildmaster.se", "08-654321", "Hammergatan 5, 112 30 Stockholm");
        Supplier colorworks = new Supplier("ColorWorks Sverige", "Emma Ekström", "emma@colorworks.se", "08-987654", "Färgvägen 9, 113 40 Stockholm");
        Supplier safety = new Supplier("Safety Wear AB","Jonas Jönsson", "jonas@safetywear.se", "08-456789", "Skyddsgatan 12, 114 50 Stockholm");
        Supplier bright = new Supplier("BrightLight AB", "Sara Svensson", "sara@brightlight.se", "08-321654",  "Belysningsvägen 7, 115 60 Stockholm");

        supplierService.saveSupplier(toolsol);
        supplierService.saveSupplier(buildmaster);
        supplierService.saveSupplier(colorworks);
        supplierService.saveSupplier(safety);
        supplierService.saveSupplier(bright);


        // --- Create & save products, each with a Set<Supplier> ---
        Product screwdriver = new Product("Screwdriver Set", "basic set of common and flathead screwdrivers for general use (6-piece).", 85.00, 180, 50, tools, toolsol);
        Product wrench = new Product("Adjustable Wrench", "Versatile 200mm adjustable wrench for various nut and bolt sizes.", 12.00, 150, 40, tools, toolsol );
        Product woodglue = new Product("Wood Glue", "500ml bottle of strong, waterproof wood glue for carpentry and repairs.", 75.00, 100, 30, build_material, buildmaster);
        Product nails = new Product("AssortedNails", "Small assortment of common nails (e.g., finishing, common) for light tasks.", 45.00, 200, 70, build_material, buildmaster);
        Product brush = new Product("Paintbrush Set", "Set of three essential paintbrushes (various sizes) for painting projects.", 60.00, 170, 60, color, colorworks);
        Product tape = new Product("Painter's Tape", "50-meter roll of medium-adhesion painter's tape for clean lines.", 40.00, 220, 80, color, colorworks);
        Product mask = new Product("DustMask", "Pack of 5 disposable dust masks for basic respiratory protection.", 35.00, 300, 100, clothes,safety);
        Product knife = new Product("UtilityKnife", "Standard retractable utility knife with a comfortable grip for cutting.", 90.00, 160, 50, tools, toolsol);
        Product led = new Product ("LEDFlashlight", "Compact LED flashlight, battery-powered, suitable for general household use.", 70.00, 250, 80, lights, bright);
        Product batteries = new Product("Batteries", "Pack of 4 standard AA alkaline batteries for various devices.", 30.00, 400, 120, lights, bright);


        screwdriver.setSuppliers(Collections.singleton(toolsol));
        productService.saveProduct(screwdriver);
        inventoryService.recordRestock(screwdriver.getId(), 100);
        inventoryService.recordSale(screwdriver.getId(), 10);

        wrench.setSuppliers(Collections.singleton(toolsol));
        productService.saveProduct(wrench);
        inventoryService.recordRestock(wrench.getId(), 80);
        inventoryService.recordSale(wrench.getId(), 5);

        woodglue.setSuppliers(Collections.singleton(buildmaster));
        productService.saveProduct(woodglue);
        inventoryService.recordRestock(woodglue.getId(), 60);
        inventoryService.recordSale(woodglue.getId(), 8);

        nails.setSuppliers(Collections.singleton(buildmaster));
        productService.saveProduct(nails);
        inventoryService.recordRestock(nails.getId(), 120);
        inventoryService.recordSale(nails.getId(), 15);

        brush.setSuppliers(Collections.singleton(colorworks));
        productService.saveProduct(brush);
        inventoryService.recordRestock(brush.getId(), 90);
        inventoryService.recordSale(brush.getId(), 12);

        tape.setSuppliers(Collections.singleton(colorworks));
        productService.saveProduct(tape);
        inventoryService.recordRestock(tape.getId(), 100);
        inventoryService.recordSale(tape.getId(), 20);

        mask.setSuppliers(Collections.singleton(safety));
        productService.saveProduct(mask);
        inventoryService.recordRestock(mask.getId(), 150);
        inventoryService.recordSale(mask.getId(), 25);

        knife.setSuppliers(Collections.singleton(toolsol));
        productService.saveProduct(knife);
        inventoryService.recordRestock(knife.getId(), 70);
        inventoryService.recordSale(knife.getId(), 10);

        led.setSuppliers(Collections.singleton(bright));
        productService.saveProduct(led);
        inventoryService.recordRestock(led.getId(), 110);
        inventoryService.recordSale(led.getId(), 18);

        batteries.setSuppliers(Collections.singleton(bright));
        productService.saveProduct(batteries);
        inventoryService.recordRestock(batteries.getId(), 200);
        inventoryService.recordSale(batteries.getId(), 40);
    }
}


