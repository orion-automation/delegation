package com.eorion.bo.enhancement.delegation.config;

import com.eorion.bo.enhancement.delegation.domain.commom.ErrorMessage;
import com.eorion.bo.enhancement.delegation.domain.exception.RequestParamException;
import com.eorion.bo.enhancement.delegation.domain.exception.UpdateFailedException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class DelegationRestControllerAdvice {
    /**
     * 修改失败
     */
    @ExceptionHandler(UpdateFailedException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "500",
                    description = "Update failed",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            )
    })
    @ResponseBody
    public ErrorMessage updateFailed(Throwable e) {
        log.error(e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    /**
     * 参数格式错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(RequestParamException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "400",
                    description = "Illegal parameter",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            )
    })
    @ResponseBody
    public ErrorMessage requestParamWrong(RequestParamException e) {
        log.error(e.getMessage());
        return new ErrorMessage("请求参数错误:" + e.getMessage());
    }
}
