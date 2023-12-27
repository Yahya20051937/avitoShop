package App.avitoShop.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.*;


@Entity
@Table(name = "Product")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private double price;
    private Category category;
    private boolean sold = false;

    private Image getMainImage(){
        for (Image image:this.images){
            System.out.println(image.getType());
            if (image.getType().toString().equals("main"))
                return image;
        }
        return this.images.get(0);
    }

    public String getEncodedImageData(){
        return Base64.getEncoder().encodeToString(this.getMainImage().getImageData());
    }
    static Map<String, Category> categoryMap = new HashMap<>();

    static {
        for (Category category1:Category.values()){
            categoryMap.put(category1.name(), category1);
        }
    }

    public Product(String name, double price, String category){
        System.out.println("Constructing Product ... ");
        this.name = name;
        this.price = price;
        this.category = categoryMap.get(category);
    }

    public Product(){}

    public enum Category {
        ELECTRONICS,
        FASHION,
        HOME_FURNITURE,
        HEALTH_BEAUTY,
        SPORTS_FITNESS,
        BOOKS,
        TOYS,
        JEWELRY,
        FOOD_BEVERAGE,
        AUTOMOTIVE,
        PET_SUPPLIES,
        ART_CRAFTS,
        MUSIC_INSTRUMENTS,
        HOME_APPLIANCES,
        GARDEN_OUTDOOR,
        TRAVEL,
        BABY_PRODUCTS,
        OFFICE_SUPPLIES,
        VIDEO_GAMES,
        SPORTING_GOODS,
        COMPUTER_SOFTWARE,
        MOBILE_PHONES,
        CAMERAS_PHOTOGRAPHY,
        WATCHES,
        LUGGAGE_BAGS,
        SHOES,
        SUNGLASSES,
        HOME_DECOR,
        KITCHEN_DINING,
        FITNESS_EQUIPMENT,
        PERSONAL_CARE,
        BEVERAGES,
        CLOTHING_ACCESSORIES,
        ELECTRICAL_TOOLS,
        GIFTS_OCCASIONS,
        HOLIDAY_SUPPLIES,
        INDUSTRIAL_SUPPLIES,
        LAB_SCIENTIFIC,
        MOVIES_TV,
        MUSICAL_INSTRUMENTS,
        PARTY_SUPPLIES,
        RELIGIOUS_SUPPLIES,
        WEDDING_SUPPLIES,
        OTHER;
    }


    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Image> images = new ArrayList<>();

    public void addImage(Image image){
        this.images.add(image);
    }

}
