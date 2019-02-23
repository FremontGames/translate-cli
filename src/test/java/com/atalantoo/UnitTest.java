package com.atalantoo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.atalantoo.translator.GoogleUIPhantomJS;
import com.atalantoo.translator.Translator;

public class UnitTest {

	@Test
	public void word() throws Exception {
		Translator translator = new GoogleUIPhantomJS();
		assertThat(translator.translate("Yes", "en", "fr")) //
				.isEqualTo("Oui");
		assertThat(translator.translate("No", "en", "fr")) //
				.isEqualTo("Non");
	}

	@Test
	public void verb() throws Exception {
		Translator translator = new GoogleUIPhantomJS();
		assertThat(translator.translate("Close", "en", "fr")) //
				.isEqualTo("Fermer");
	}

	@Test
	public void name() throws Exception {
		Translator translator = new GoogleUIPhantomJS();
		assertThat(translator.translate("Cars2048", "en", "fr")) //
				.isEqualTo("Cars2048");
	}


	@Test
	public void expression() throws Exception {
		Translator translator = new GoogleUIPhantomJS();
		assertThat(translator.translate("Next level", "en", "fr")) //
				.isEqualTo("Niveau suivant");
		assertThat(translator.translate("Blue car", "en", "fr")) //
				.isEqualTo("Voiture bleue");
	}

	@Test
	public void sentence() throws Exception {
		Translator translator = new GoogleUIPhantomJS();
		assertThat(translator.translate("Do you really want to quit this application?", "en", "fr")) //
				.isEqualTo("Voulez-vous vraiment quitter cette application?");
	}

	@Test
	public void lines() throws Exception {
		Translator translator = new GoogleUIPhantomJS();
		assertThat(translator.translate("Do you really want to quit this application?\n " //
				+ "The current game will not be saved.", "en", "fr")) //
						.isEqualTo("Voulez-vous vraiment quitter cette application?\n  "
								+ "La partie en cours ne sera pas sauvegardée.");
	}

	@Test
	public void masculin() throws Exception {
		Translator translator = new GoogleUIPhantomJS();
		assertThat(translator.translate("beginner", "en", "fr")) //
				.isEqualTo("débutant");
	}

}
