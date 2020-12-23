package org.sid.sec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


public class JWTAuthorizationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//Donner l'autorisation de transfert et envoie des requetes entre des domaines differents
		//pour régler le probleme de CORS (Cross-Origin-Request-Shering)
		response.addHeader("Access-Control-Allow-Origin", "*");//"*" autorise tous les domaines
		
		//Donner l'autorisation à utiliser des entêtes par la partie client (comme Angular).Pour ne pas envoyer n'importe quel entête.
		response.addHeader("Access-Control-Allow-Headers","Origin, Accept,"
							+ "X-Requested-With,"
							+ "Content-Type,"
							+ " Access-Control-Request-Method,"
							+ " Access-Control-Request-Headers,"
							+ " Authorization");
		
		//Donner la permission de la lecture des entêtes dans la partie cliente (front-end) par:
		response.addHeader("Access-Control-Expose-Headers",
							"Access-Control-Allow-Origin, "
							+ "Access-Control-Allow-Credentials,"
							+ " Authorization");
		
		/*si on envoie une requete avec 'post' dans un domaine (front-end) vers un autre domaine different (back-end)
		 * alors avant que la requete 'post' soit envoyer, Angular par exemple envoie d'abord une requete avec "OPTIONS"
		 * "OPTIONS": est une méthode utilisée dans HTTP client permet d'interoger le server 
		 * 				pour fournir les options de communication à utiliser (comme Cross-Origin,..)
		 * */
		if (request.getMethod().equals("OPTIONS")) { //ce test nous permet d'autorisé les requetes avec options (si détecter)
			response.setStatus(HttpServletResponse.SC_OK);//donner à "OPTIONS" le statu "OK" et répondre avec le traitement en haut
		}
		
		else {
			
			String jwt = request.getHeader(SecurityConstants.HEADER_STRING);
			System.out.println(jwt);
			
			if (jwt==null || !jwt.startsWith(SecurityConstants.TOKEN_PREFIX)) {
				filterChain.doFilter(request, response); 
				return;
			}
		
			//getter le contenue de mon token
			Claims claims = Jwts.parser()
					.setSigningKey(SecurityConstants.SECRET)
					.parseClaimsJws(jwt.replace(SecurityConstants.TOKEN_PREFIX, ""))
					.getBody();
			//recuperer le username à partir du teken obtenu en haut par subject car :
			String username = claims.getSubject(); // {"sub" : "username"}
			
			//recuperer les rôles partir du teken	
			@SuppressWarnings("unchecked")
			ArrayList<Map<String, String>> roles = (ArrayList<Map<String, String>>) claims.get("roles");
			
			//convertir les rôles obtenus du token à une collection de GrantedAuthority
			//pour que spring security puisse charger le context
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			roles.forEach(r->{
				authorities.add(new SimpleGrantedAuthority(r.get("authority")));
			});
			
			//création d'un object de type usernamepasswordauthentitoken 
			//contenant tout nos donné nécessaires pour charger le context spring security 
			//c à dire on va creer un utilisateur authentifié
			UsernamePasswordAuthenticationToken authenticatedUser = 
				new UsernamePasswordAuthenticationToken(username, null, authorities);//null dans le password car on l'a pas besoin 
			
			//aprés obtention de toutes les données on va charger le context spring security par la classe securityholder
			//c à dire on va charger l'utilisateur authentifié
			SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
			//charger l'identité de chaque user envoyant une requete
			filterChain.doFilter(request, response);
	}

}
}
