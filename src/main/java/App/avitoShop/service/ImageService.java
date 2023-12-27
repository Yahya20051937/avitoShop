package App.avitoShop.service;

import App.avitoShop.data.ImageRepository;
import App.avitoShop.entity.Image;
import App.avitoShop.entity.Product;
import App.avitoShop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
@Service
public class ImageService {

    private ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository){
        this.imageRepository = imageRepository;
    }

    public void saveProductImage(MultipartFile file, Product product, Image.Type type){
        if (!file.isEmpty()){
            try {
                byte[] bytes = file.getBytes();
                Image image = new Image();
                image.setImageData(bytes);
                image.setType(type);
                image.setProduct(product);
                product.addImage(image);
            }
            catch (IOException e){
            }
        }

    }

    public void saveUserImage(MultipartFile imageFile, User user){
        if (!imageFile.isEmpty()){
            try {
                byte[] bytes = imageFile.getBytes();
                Image image = new Image();
                image.setImageData(bytes);
                image.setType(Image.Type.main);
                this.imageRepository.save(image);
                user.setImage(image);
            }
            catch (IOException e){}
        }
        // No image was selected, so we set the default image
        else{
            Path defaultImagePath = Paths.get("C:\\avitoShop\\src\\main\\resources\\static\\images\\defaultImage.png");
            try {
                byte[] bytes = Files.readAllBytes(defaultImagePath);
                Image defaultImage = new Image();
                defaultImage.setImageData(bytes);
                defaultImage.setType(Image.Type.main);
                this.imageRepository.save(defaultImage);
                user.setImage(defaultImage);
            }
            catch (IOException e){}
        }
    }
}
