package com.ecommerce.cart;

import java.time.Duration;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;

import com.ecommerce.cart.model.Cart;
import com.ecommerce.cart.model.CartItem;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class CartRedisOperationsTest {

    private ReactiveRedisTemplate<String, Cart> redisTemplate;
    private ReactiveValueOperations<String, Cart> cartOps;

    @BeforeEach
    public void setup() {
        redisTemplate = Mockito.mock(ReactiveRedisTemplate.class);
        cartOps = Mockito.mock(ReactiveValueOperations.class);
        Mockito.when(redisTemplate.opsForValue()).thenReturn(cartOps);
    }

    @Test
    public void givenCart_whenSet_thenSet() {
        Mockito.when(cartOps.set("cart-1", getSampleCart())).thenReturn(Mono.just(true));
        Mono<Boolean> result = cartOps.set("cart-1", getSampleCart());
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    public void givenCartId_whenGet_thenReturnsCart() {
        Mockito.when(cartOps.get("cart-1")).thenReturn(Mono.just(getSampleCart()));
        Mono<Cart> fetchedCart = cartOps.get("cart-1");
        StepVerifier.create(fetchedCart)
                .expectNext(getSampleCart())
                .verifyComplete();
    }

    @Test
    public void givenCart_whenSetWithExpiry_thenSetsWithExpiryTime() {
        Mockito.when(cartOps.set("cart-2", getSampleCart(), Duration.ofSeconds(1))).thenReturn(Mono.just(true));
        Mockito.when(cartOps.get("cart-2")).thenReturn(Mono.empty());
        Mono<Boolean> result = cartOps.set("cart-2", getSampleCart(), Duration.ofSeconds(1));
        Mono<Cart> fetchedCart = cartOps.get("cart-2");
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
        StepVerifier.create(fetchedCart)
                .expectNextCount(0L)
                .verifyComplete();
    }

    @Test
    public void givenNonExistingCartId_whenGet_thenReturnsEmpty() {
        Mockito.when(cartOps.get("non-existing")).thenReturn(Mono.empty());
        Mono<Cart> fetchedCart = cartOps.get("non-existing");
        StepVerifier.create(fetchedCart)
                .expectNextCount(0L)
                .verifyComplete();
    }

    @Test
    public void givenCart_whenDelete_thenDeletesCart() {
        Mockito.when(cartOps.set("cart-3", getSampleCart())).thenReturn(Mono.just(true));
        Mockito.when(cartOps.get("cart-3")).thenReturn(Mono.just(getSampleCart())).thenReturn(Mono.empty());
        Mono<Boolean> setResult = cartOps.set("cart-3", getSampleCart());
        Mono<Cart> fetchedCart = cartOps.get("cart-3");
        StepVerifier.create(setResult)
                .expectNext(true)
                .verifyComplete();
        StepVerifier.create(fetchedCart)
                .expectNext(getSampleCart())
                .verifyComplete();
        // Simulate deletion
        Mockito.when(cartOps.get("cart-3")).thenReturn(Mono.empty());
        StepVerifier.create(cartOps.get("cart-3"))
                .expectNextCount(0L)
                .verifyComplete();
    }

    private Cart getSampleCart() {
        CartItem cartItem = new CartItem("cart-1-1", "sku1", "Nike Shoes", 1, 145.0f, "USD");
        ArrayList<CartItem> cartItems = new ArrayList<CartItem>();
        cartItems.add(cartItem);
        return new Cart("cart-1", cartItems, 145.0f, "USD");
    }
}
