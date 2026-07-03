package com.hotelreservas.service;

import com.hotelreservas.dto.LoginRequestDTO;
import com.hotelreservas.dto.LoginResponseDTO;
import com.hotelreservas.dto.RegisterRequestDTO;
import com.hotelreservas.model.Usuario;

public interface IAuthService {
    LoginResponseDTO login(LoginRequestDTO request);
    Usuario register(RegisterRequestDTO request);
}
