package main;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.stefanbirkner.systemlambda.SystemLambda;

public class SystemTest {

  private Zugriffsdialog dialog;

  @BeforeEach
  public void setUp() {
    this.dialog = new Zugriffsdialog();
  }

  private void neuesBild(String serie, int nr) throws Exception {
    String[] inputs = { serie, "" + nr };
    SystemLambda.withTextFromSystemIn(inputs)
        .execute(() -> {
          this.dialog.sammelbildHinzufuegen();
        });
  }

  private void bildHerausgeben(String serie, int nr, int anzahl)
      throws Exception {
    String[] inputs = { serie, "" + nr, "" + anzahl };
    SystemLambda.withTextFromSystemIn(inputs)
        .execute(() -> {
          this.dialog.sammelbilderHerausgeben();
        });
  }

  private void serieZeigen(String serie) throws Exception {
    String[] inputs = { serie };
    SystemLambda.withTextFromSystemIn(inputs)
        .execute(() -> {
          this.dialog.serienbilderZeigen();
        });
  }

  private void basisdaten() throws Exception {
    Object[][] basis = { { "WM 2014", 123, 2 }, { "EM 2016", 42, 3 },
        { "EM 2016", 71, 1 }
    };
    for (Object[] eingabe : basis) {
      for (int i = 0; i < (int) eingabe[2]; i++) {
        this.neuesBild(eingabe[0].toString(), (int) eingabe[1]);
      }
    }
  }

  @Test
  public void testHinzu1() throws Exception {
    this.neuesBild("XYZ", 100);
    String systemOut = SystemLambda.tapSystemOutNormalized(() -> {
      this.dialog.gesamtbestand();
    });
    Assertions.assertTrue(systemOut.contains("XYZ")
        , "Bild (XYZ,100), XYZ nicht befunden ");
    Assertions.assertTrue(systemOut.contains("100")
        , "Bild (XYZ,100), 100 nicht befunden ");
  }

  @Test
  public void testHinzu2() throws Exception {
    this.neuesBild("XYZ", 100);
    this.neuesBild("XYZ", 100);
    String systemOut = SystemLambda.tapSystemOutNormalized(() -> {
      this.dialog.gesamtbestand();
    });
    Assertions.assertFalse(
        Pattern.matches("(?s).*100.*100.*", systemOut)
        , "doppeltes Bild (XYZ,100) nicht zweimal ausgeben");
  }

  @Test
  public void testStandardausgabeVonMultisetGenutzt()
      throws Exception {
    this.basisdaten();
    String systemOut = SystemLambda.tapSystemOutNormalized(() -> {
      this.dialog.gesamtbestand();
    });
    Assertions.assertTrue(systemOut.contains("x 2")
        , "Standardausgabe 'x 2' fuer Doppelte nutzen ");
    Assertions.assertTrue(systemOut.contains("x 3")
        , "Standardausgabe 'x 3' fuer Dreifache nutzen ");
  }

  @Test
  public void testHinzuMehrfach() throws Exception {
    this.basisdaten();
    this.neuesBild("EM 2016", 42);
    String systemOut = SystemLambda.tapSystemOutNormalized(() -> {
      this.dialog.gesamtbestand();
    });
    Assertions.assertTrue(systemOut.contains("x 4")
        , "'x 4' fuer vierfaches Bild nicht gefunden ");
  }

  @Test
  public void testSerieZeigen1() throws Exception {
    String systemOut = SystemLambda.tapSystemOutNormalized(() -> {
      this.serieZeigen("ABC");
    });
    Assertions.assertFalse(
        Pattern.matches("(?s).*[0-9].*", systemOut)
        , "Bildnummer in nicht existenter Serie gefunden");
  }

  @Test
  public void testserieZeigen2() throws Exception {
    this.basisdaten();
    String systemOut = SystemLambda.tapSystemOutNormalized(() -> {
      this.serieZeigen("EM 2016");
    });
    Assertions.assertFalse(systemOut.contains("x 2")
        , "keine doppelte Nummer fuer EM 16 in Basisdaten");
    Assertions.assertTrue(systemOut.contains("x 3")
        , "Standardausgabe 'x 3' fuer dreifache Nummern nutzen ");
  }

  @Test
  public void testHerausgeben1() throws Exception {
    String systemOut = SystemLambda.tapSystemOutNormalized(() -> {
      this.bildHerausgeben("XYZ", 1, 1);
    });
    Assertions.assertTrue(systemOut.contains("nicht erfolgreich")
        , "nicht existentes Bild herausgegeben");
  }

  @Test
  public void testHerausgeben2() throws Exception {
    this.basisdaten();
    String systemOut = SystemLambda.tapSystemOutNormalized(() -> {
      this.bildHerausgeben("EM 2016", 42, 3);
      this.dialog.gesamtbestand();
    });
    Assertions.assertFalse(systemOut.contains("nicht erfolgreich")
        , "existierende Bilder nicht herausgegeben: "
            + systemOut);
  }

  @Test
  public void testHerausgeben3() throws Exception {
    this.basisdaten();
    String systemOut = SystemLambda.tapSystemOutNormalized(() -> {
      this.bildHerausgeben("EM 2016", 42, 1);
      this.dialog.gesamtbestand();
    });
    Assertions.assertFalse(systemOut.contains("nicht erfolgreich"),
        "existierende Bilder nicht herausgegeben");
    Assertions.assertTrue(
        Pattern.matches("(?s).*x 2.*x 2.*", systemOut)
        , "Bildanzahl von (EM 2016, 42) nicht korrekt reduziert; "
            + systemOut);
  }

  @Test
  public void testHerausgeben4() throws Exception {
    this.basisdaten();
    this.neuesBild("EM 2016", 42);
    String systemOut = SystemLambda.tapSystemOutNormalized(() -> {
      this.bildHerausgeben("EM 2016", 42, 2);
      this.dialog.gesamtbestand();
    });
    Assertions.assertFalse(systemOut.contains("nicht erfolgreich")
        , "existierende Bilder nicht herausgegeben");
    Assertions.assertTrue(
        Pattern.matches("(?s).*x 2.*x 2.*", systemOut)
        ,"Bildanzahl von (EM 2016, 42) nicht korrekt reduziert; "
              + systemOut);
  }
}
