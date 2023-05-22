package com.android.petopia.network;

import com.android.petopia.model.Cart;
import com.android.petopia.model.CartItem;
import com.android.petopia.model.DataOrder;
import com.android.petopia.model.LoginData;
import com.android.petopia.model.Order;
import com.android.petopia.model.Pet;
import com.android.petopia.model.PostLogin;
import com.android.petopia.model.PostRegister;
import com.android.petopia.model.Product;
import com.android.petopia.model.Service;
import com.android.petopia.model.UpdateProfileData;
import com.android.petopia.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    String URL = "http://10.0.2.2:8081/api/v1/";

    @POST("register")
    Call<User> apiRegister(@Body PostRegister postRegister);

    @POST("login")
    Call<LoginData> apiLogin(@Body PostLogin postLogin);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("users/profile")
    Call<User> getUser(@Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @PUT("users/profile/update")
    Call<User> updateUser(@Body UpdateProfileData updateProfileData, @Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("users/blogs/create")
    Call<Pet> createPet(@Body Pet pet, @Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("users/blogs/filter")
    Call<List<Pet>> getPetCatePet(@Query("catePet") Integer catePet, @Query("status") String status, @Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("users/blogs/filter")
    Call<List<Pet>> getMyPetCatePet(@Query("user") Long userId,@Query("catePet") Integer catePet, @Query("status") String status, @Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @PUT("users/blogs/delete/{id}")
    Call<Pet> deletePet(@Path("id") Long id, @Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("users/location/create")
    Call<Service> createService(@Body Service service, @Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("users/location/filter")
    Call<List<Service>> getMyService(@Query("user") Long userId, @Query("status") String status, @Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @PUT("users/location/delete/{id}")
    Call<Service> deleteService(@Path("id") Long id, @Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("users/products")
    Call<List<Product>> apiGetListProduct(@Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("users/location/filter")
    Call<List<Service>> getService(@Query("typeLocation") Integer typeLocationId, @Query("status") String status, @Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("users/products/filter")
    Call<List<Product>> getProductFilter(@Query("category") Integer categoriesId, @Query("status") String status, @Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("users/cart/add")
    Call<Cart> addToCart(@Query("productId") Long productId, @Query("quantity") Integer quantity, @Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("users/cart")
    Call<List<CartItem>> getCart(@Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @PUT("users/cart/update")
    Call<Cart> updateCart(@Query("shoppingCatId") Long shoppingCatId,@Query("productId") Long productId, @Query("quantity") Integer quantity,@Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @PUT("users/cart/delete")
    Call<Cart> deleteCart(@Query("shoppingCatId") Long shoppingCatId,@Query("productId") Long productId, @Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("users/orders/checkout")
    Call<Order> order(@Query("shoppingCartId") Long shoppingCartId,@Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("users/orders/getmyorder")
    Call<DataOrder> getMyOrderFilter(@Query("user") Long user, @Query("status") String status, @Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("users/orders/getmyorder")
    Call<DataOrder> getMyOrderAll(@Query("user") Long user, @Header("Authorization") String token);
}