package de.lmu.ifi.dbs.elki.math.statistics.distribution;

/*
 This file is part of ELKI:
 Environment for Developing KDD-Applications Supported by Index-Structures

 Copyright (C) 2014
 Ludwig-Maximilians-Universität München
 Lehr- und Forschungseinheit für Datenbanksysteme
 ELKI Development Team

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import org.junit.Test;

import de.lmu.ifi.dbs.elki.JUnit4Test;

/**
 * Unit test for the Generalized Logistic distribution in ELKI.
 * 
 * The reference values were computed using GNU R and SciPy.
 * 
 * @author Erich Schubert
 */
public class TestGeneralizedLogisticDistribution extends AbstractDistributionTest implements JUnit4Test {
  public static final double[] P_CDFPDF = { //
  1e-10, 1e-05, 0.1, 0.1234567, 0.2, 0.271828182846, 0.3, 0.314159265359, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0, 2.71828182846, 3.14159265359 //
  };

  public static final double[] SCIPY_LOGISTIC_CDF_05 = { //
  3.77540668821645775121709220911725424230098724365234e-01, // 0.000000
  3.77543018838145283400109519789111800491809844970703e-01, // 0.000010
  4.01312339887547997463457249978091567754745483398438e-01, // 0.100000
  4.06960880864213336849388724658638238906860351562500e-01, // 0.123457
  4.25557483188341023616629854586790315806865692138672e-01, // 0.200000
  4.43203246664875705196351418635458685457706451416016e-01, // 0.271828
  4.50166002687522159853017456043744459748268127441406e-01, // 0.300000
  4.53673071561317309274841136357281357049942016601562e-01, // 0.314159
  4.75020812521059987432181515032425522804260253906250e-01, // 0.400000
  5.00000000000000000000000000000000000000000000000000e-01, // 0.500000
  5.24979187478939901545516022451920434832572937011719e-01, // 0.600000
  5.49833997312477840146982543956255540251731872558594e-01, // 0.700000
  5.74442516811659031894521376671036705374717712402344e-01, // 0.800000
  5.98687660112452002536542750021908432245254516601562e-01, // 0.900000
  6.22459331201854593196287623868556693196296691894531e-01, // 1.000000
  6.45656306225795395548061605950351804494857788085938e-01, // 1.100000
  6.68187772168166160824398502882104367017745971679688e-01, // 1.200000
  6.89974481127612504494095446716528385877609252929688e-01, // 1.300000
  7.10949502625003892930521942616906017065048217773438e-01, // 1.400000
  7.31058578630004896048433238320285454392433166503906e-01, // 1.500000
  7.50260105595117687826700603181961923837661743164062e-01, // 1.600000
  7.68524783499017538623832024313742294907569885253906e-01, // 1.700000
  7.85834983042558610222272363898809999227523803710938e-01, // 1.800000
  8.02183888558581692507232219213619828224182128906250e-01, // 1.900000
  8.17574476193643651100728675373829901218414306640625e-01, // 2.000000
  9.01879254389253204315934908663621172308921813964844e-01, // 2.718282
  9.33490913616609740977025921893073245882987976074219e-01, // 3.141593
  };

  public static final double[] SCIPY_LOGISTIC_PDF_05 = { //
  2.35003712207350168306163595843827351927757263183594e-01, // 0.000000
  2.35004287764725150466915692959446460008621215820312e-01, // 0.000010
  2.40260745741529169183792191688553430140018463134766e-01, // 0.100000
  2.41343722310436908928821253539354074746370315551758e-01, // 0.123457
  2.44458311690745860866869065830542240291833877563477e-01, // 0.200000
  2.46774128810589049587420618081523571163415908813477e-01, // 0.271828
  2.47516572711860005640005510940682142972946166992188e-01, // 0.300000
  2.47853815701437218965708098039613105356693267822266e-01, // 0.314159
  2.49376040192891945679320997442118823528289794921875e-01, // 0.400000
  2.50000000000000000000000000000000000000000000000000e-01, // 0.500000
  2.49376040192891945679320997442118823528289794921875e-01, // 0.600000
  2.47516572711859950128854279682855121791362762451172e-01, // 0.700000
  2.44458311690745888622444681459455750882625579833984e-01, // 0.800000
  2.40260745741529169183792191688553430140018463134766e-01, // 0.900000
  2.35003712201594494590750628049136139452457427978516e-01, // 1.000000
  2.28784240456657267381856968313513789325952529907227e-01, // 1.100000
  2.21712873293109097305730870175466407090425491333008e-01, // 1.200000
  2.13909696520294428934150232635147403925657272338867e-01, // 1.300000
  2.05500307342263432985873805591836571693420410156250e-01, // 1.400000
  1.96611933241481878775758218580449465662240982055664e-01, // 1.500000
  1.87369879547520601370536041940795257687568664550781e-01, // 1.600000
  1.77894440646805707118005557276774197816848754882812e-01, // 1.700000
  1.68298362469060241997098614774586167186498641967773e-01, // 1.800000
  1.58684897495614651852235965634463354945182800292969e-01, // 1.900000
  1.49146452070332835582178176991874352097511291503906e-01, // 2.000000
  8.84930648915379924890345364474342204630374908447266e-02, // 2.718282
  6.20856278118370533136705091692419955506920814514160e-02, // 3.141593
  };

  public static final double[] GNUR_LOGISTIC_CDF_05 = { //
  3.77540668821645775121709220911725424230098724365234e-01, // 0.000000
  3.77543018838145283400109519789111800491809844970703e-01, // 0.000010
  4.01312339887547997463457249978091567754745483398438e-01, // 0.100000
  4.06960880864213336849388724658638238906860351562500e-01, // 0.123457
  4.25557483188341023616629854586790315806865692138672e-01, // 0.200000
  4.43203246664899241924473471954115666449069976806641e-01, // 0.271828
  4.50166002687522159853017456043744459748268127441406e-01, // 0.300000
  4.53673071561322416300754412077367305755615234375000e-01, // 0.314159
  4.75020812521059987432181515032425522804260253906250e-01, // 0.400000
  5.00000000000000000000000000000000000000000000000000e-01, // 0.500000
  5.24979187478939901545516022451920434832572937011719e-01, // 0.600000
  5.49833997312477840146982543956255540251731872558594e-01, // 0.700000
  5.74442516811659031894521376671036705374717712402344e-01, // 0.800000
  5.98687660112452002536542750021908432245254516601562e-01, // 0.900000
  6.22459331201854593196287623868556693196296691894531e-01, // 1.000000
  6.45656306225795395548061605950351804494857788085938e-01, // 1.100000
  6.68187772168166160824398502882104367017745971679688e-01, // 1.200000
  6.89974481127612504494095446716528385877609252929688e-01, // 1.300000
  7.10949502625003892930521942616906017065048217773438e-01, // 1.400000
  7.31058578630004896048433238320285454392433166503906e-01, // 1.500000
  7.50260105595117687826700603181961923837661743164062e-01, // 1.600000
  7.68524783499017538623832024313742294907569885253906e-01, // 1.700000
  7.85834983042558610222272363898809999227523803710938e-01, // 1.800000
  8.02183888558581692507232219213619828224182128906250e-01, // 1.900000
  8.17574476193643651100728675373829901218414306640625e-01, // 2.000000
  9.01879254389337470243503958045039325952529907226562e-01, // 2.718282
  9.33490913616622508541809111193288117647171020507812e-01, // 3.141593
  };

  public static final double[] GNUR_LOGISTIC_PDF_05 = { //
  2.35003712207350168306163595843827351927757263183594e-01, // 0.000000
  2.35004287764725178222491308588359970599412918090820e-01, // 0.000010
  2.40260745741529169183792191688553430140018463134766e-01, // 0.100000
  2.41343722310436908928821253539354074746370315551758e-01, // 0.123457
  2.44458311690745888622444681459455750882625579833984e-01, // 0.200000
  2.46774128810591714122679718457220587879419326782227e-01, // 0.271828
  2.47516572711859977884429895311768632382154464721680e-01, // 0.300000
  2.47853815701437635299342332473315764218568801879883e-01, // 0.314159
  2.49376040192891945679320997442118823528289794921875e-01, // 0.400000
  2.50000000000000000000000000000000000000000000000000e-01, // 0.500000
  2.49376040192891945679320997442118823528289794921875e-01, // 0.600000
  2.47516572711859950128854279682855121791362762451172e-01, // 0.700000
  2.44458311690745888622444681459455750882625579833984e-01, // 0.800000
  2.40260745741529169183792191688553430140018463134766e-01, // 0.900000
  2.35003712201594494590750628049136139452457427978516e-01, // 1.000000
  2.28784240456657267381856968313513789325952529907227e-01, // 1.100000
  2.21712873293109097305730870175466407090425491333008e-01, // 1.200000
  2.13909696520294428934150232635147403925657272338867e-01, // 1.300000
  2.05500307342263432985873805591836571693420410156250e-01, // 1.400000
  1.96611933241481878775758218580449465662240982055664e-01, // 1.500000
  1.87369879547520601370536041940795257687568664550781e-01, // 1.600000
  1.77894440646805707118005557276774197816848754882812e-01, // 1.700000
  1.68298362469060241997098614774586167186498641967773e-01, // 1.800000
  1.58684897495614651852235965634463354945182800292969e-01, // 1.900000
  1.49146452070332835582178176991874352097511291503906e-01, // 2.000000
  8.84930648914700607177152846816170495003461837768555e-02, // 2.718282
  6.20856278118259094500608341604674933478236198425293e-02, // 3.141593
  };

  public static final double[] SCIPY_GLOGISTIC_CDF_2_05 = { //
  1.42536956614295634571121240696811582893133163452148e-01, // 0.000000
  1.42538731073420105177618211200751829892396926879883e-01, // 0.000010
  1.61051594146018856035240673918451648205518722534180e-01, // 0.100000
  1.65617158553776427920567471119284164160490036010742e-01, // 0.123457
  1.81099171497595134994185173127334564924240112304688e-01, // 0.200000
  1.96429117854286627853355184925021603703498840332031e-01, // 0.271828
  2.02649429975662181968587560731975827366113662719727e-01, // 0.300000
  2.05819255859880118064708653946581762284040451049805e-01, // 0.314159
  2.25644772328168013997284901961393188685178756713867e-01, // 0.400000
  2.50000000000000000000000000000000000000000000000000e-01, // 0.500000
  2.75603147286047955866195025009801611304283142089844e-01, // 0.600000
  3.02317424600617945529279495531227439641952514648438e-01, // 0.700000
  3.29984205120913143272076695211580954492092132568359e-01, // 0.800000
  3.58426914370922833352750558333355002105236053466797e-01, // 0.900000
  3.87455619000260098605536995819420553743839263916016e-01, // 1.000000
  4.16872065769138100410629022007924504578113555908203e-01, // 1.100000
  4.46474898875057091274243248335551470518112182617188e-01, // 1.200000
  4.76064784607318047804369598452467471361160278320312e-01, // 1.300000
  5.05449195282740459944648137025069445371627807617188e-01, // 1.400000
  5.34446645388523045028250635368749499320983886718750e-01, // 1.500000
  5.62890226047597086456164561241166666150093078613281e-01, // 1.600000
  5.90630342852211831505826467036968097090721130371094e-01, // 1.700000
  6.17536620573498451491900596010964363813400268554688e-01, // 1.800000
  6.43498991062967040654996253579156473278999328613281e-01, // 1.900000
  6.68428024123310704496248035866301506757736206054688e-01, // 2.000000
  8.13386189497715239582475987845100462436676025390625e-01, // 2.718282
  8.71405285804772722357824932259973138570785522460938e-01, // 3.141593
  };

  public static final double[] SCIPY_GLOGISTIC_PDF_2_05 = { //
  1.77446917364665113225896675430703908205032348632812e-01, // 0.000000
  1.77448456485205074972810734834638424217700958251953e-01, // 0.000010
  1.92839204113320589195623711020743940025568008422852e-01, // 0.100000
  1.96434907645006984200364286152762360870838165283203e-01, // 0.123457
  2.08062127735169605236009715554246213287115097045898e-01, // 0.200000
  2.18742190163498606558789560949662700295448303222656e-01, // 0.271828
  2.22847092273226854430845378374215215444564819335938e-01, // 0.300000
  2.24889203734927378697250333061674609780311584472656e-01, // 0.314159
  2.36917618471424035186956302823091391474008560180664e-01, // 0.400000
  2.50000000000000000000000000000000000000000000000000e-01, // 0.500000
  2.61834461914359772904958845174405723810195922851562e-01, // 0.600000
  2.72186053150493101338014412249322049319744110107422e-01, // 0.700000
  2.80854495646322144253304031735751777887344360351562e-01, // 0.800000
  2.87682287369737721416385056727449409663677215576172e-01, // 0.900000
  2.92560507053915230812890513334423303604125976562500e-01, // 1.000000
  2.95431975231839061279970337636768817901611328125000e-01, // 1.100000
  2.96291661733450895077623954421142116189002990722656e-01, // 1.200000
  2.95184463729510404572664583611185662448406219482422e-01, // 1.300000
  2.92200682588535265438167698448523879051208496093750e-01, // 1.400000
  2.87469680914430258944491924921749159693717956542969e-01, // 1.500000
  2.81152291229334549971241585808456875383853912353516e-01, // 1.600000
  2.73432572967530318486240048514446243643760681152344e-01, // 1.700000
  2.64509481633928678068201634232536889612674713134766e-01, // 1.800000
  2.54588936257104159199826654003118164837360382080078e-01, // 1.900000
  2.43876664855085467653594832881935872137546539306641e-01, // 2.000000
  1.59620118766000157473072817992942873388528823852539e-01, // 2.718282
  1.15912738857065128472711990070820320397615432739258e-01, // 3.141593
  };

  public static final double[] SCIPY_GLOGISTIC_CDF_05_05 = { //
  6.14443381298591106975948150648036971688270568847656e-01, // 0.000000
  6.14445293608914644423180106969084590673446655273438e-01, // 0.000010
  6.33492178237070269197772631741827353835105895996094e-01, // 0.100000
  6.37934856285666040953685751446755602955818176269531e-01, // 0.123457
  6.52347670485869834955394708231324329972267150878906e-01, // 0.200000
  6.65735117494094419399175421858672052621841430664062e-01, // 0.271828
  6.70944112939015346874782608210807666182518005371094e-01, // 0.300000
  6.73552575202053027680904051521793007850646972656250e-01, // 0.314159
  6.89217536428854971042312627105275169014930725097656e-01, // 0.400000
  7.07106781186547572737310929369414225220680236816406e-01, // 0.500000
  7.24554475163144640070811419718666002154350280761719e-01, // 0.600000
  7.41507921274262460364923299493966624140739440917969e-01, // 0.700000
  7.57919861734510202921910604345612227916717529296875e-01, // 0.800000
  7.73749093771651574868997158773709088563919067382812e-01, // 0.900000
  7.88960918678393463565612364618573337793350219726562e-01, // 1.000000
  8.03527414731940226033657381776720285415649414062500e-01, // 1.100000
  8.17427533282410356818559193925466388463973999023438e-01, // 1.200000
  8.30647025593670873533369558572303503751754760742188e-01, // 1.300000
  8.43178215222027627184786524594528600573539733886719e-01, // 1.400000
  8.55019636400243698837186911987373605370521545410156e-01, // 1.500000
  8.66175562801859255657177527609746903181076049804688e-01, // 1.600000
  8.76655453127976325156112125114304944872856140136719e-01, // 1.700000
  8.86473340288673639086880484683206304907798767089844e-01, // 1.800000
  8.95647189778755237199447947205044329166412353515625e-01, // 1.900000
  9.04198250492470045891479912825161591172218322753906e-01, // 2.000000
  9.49673235586458552859312476357445120811462402343750e-01, // 2.718282
  9.66173335181948478300739679980324581265449523925781e-01, // 3.141593
  };

  public static final double[] SCIPY_GLOGISTIC_PDF_05_05 = { //
  1.91233008085043759693988363324024248868227005004883e-01, // 0.000000
  1.91232881274457228171215206202759873121976852416992e-01, // 0.000010
  1.89631974944145992312272142044093925505876541137695e-01, // 0.100000
  1.89160162618833033176812818965117912739515304565430e-01, // 0.123457
  1.87368118835062935634283576291636563837528228759766e-01, // 0.200000
  1.85339576000944639133649616269394755363464355468750e-01, // 0.271828
  1.84453941795266723735480240975448396056890487670898e-01, // 0.300000
  1.83989954776051262141933761995460372418165206909180e-01, // 0.314159
  1.80912431135328455322763829826726578176021575927734e-01, // 0.400000
  1.76776695296636865428752116713440045714378356933594e-01, // 0.500000
  1.72089227753883550198210627968364860862493515014648e-01, // 0.600000
  1.66900828440584320100370518957788590341806411743164e-01, // 0.700000
  1.61269234409096795523907985625555738806724548339844e-01, // 0.800000
  1.55257529653685638626470222334319259971380233764648e-01, // 0.900000
  1.48932416446719950720023462054086849093437194824219e-01, // 1.000000
  1.42362436092476424276398461188364308327436447143555e-01, // 1.100000
  1.35616225454758571045132953258871566504240036010742e-01, // 1.200000
  1.28760887554741604299479718065413180738687515258789e-01, // 1.300000
  1.21860541242844275244827656479174038395285606384277e-01, // 1.400000
  1.14975098156368985002906413228629389777779579162598e-01, // 1.500000
  1.08159296795112963573970432662463281303644180297852e-01, // 1.600000
  1.01462005404782576456490517102793091908097267150879e-01, // 1.700000
  9.49257889776217583799322596860292833298444747924805e-02, // 1.800000
  8.85867221527336656805573511519469320774078369140625e-02, // 1.900000
  8.24744197354399105615030407534504774957895278930664e-02, // 2.000000
  4.65913229811568962990264708423637785017490386962891e-02, // 2.718282
  3.21296529054722751483907927649852354079484939575195e-02, // 3.141593
  };

  public static final double[] P_QUANT = { //
  0.0001, 0.001, 0.01, 0.1, 0.25, 0.5, 0.75, 0.9, 0.99, 0.999, 0.9999 //
  };

  public static final double[] SCIPY_LOGISTIC_QUANT_05 = { //
  -8.71024036697584946864481025841087102890014648437500e+00, // 0.000100
  -6.40675477864855391629816949716769158840179443359375e+00, // 0.001000
  -4.09511985013458978954759004409424960613250732421875e+00, // 0.010000
  -1.69722457733621956421643517387565225362777709960938e+00, // 0.100000
  -5.98612288668109782108217586937826126813888549804688e-01, // 0.250000
  5.00000000000000000000000000000000000000000000000000e-01, // 0.500000
  1.59861228866811000415282251196913421154022216796875e+00, // 0.750000
  2.69722457733621912012722532381303608417510986328125e+00, // 0.900000
  5.09511985013458357229865214321762323379516601562500e+00, // 0.990000
  7.40675477864846509845619948464445769786834716796875e+00, // 0.999000
  9.71024036697667902728881017537787556648254394531250e+00, // 0.999900
  };

  public static final double[] GNUR_LOGISTIC_QUANT_05 = { //
  -8.71024036697584946864481025841087102890014648437500e+00, // 0.000100
  -6.40675477864855391629816949716769158840179443359375e+00, // 0.001000
  -4.09511985013458978954759004409424960613250732421875e+00, // 0.010000
  -1.69722457733621912012722532381303608417510986328125e+00, // 0.100000
  -5.98612288668109782108217586937826126813888549804688e-01, // 0.250000
  5.00000000000000000000000000000000000000000000000000e-01, // 0.500000
  1.59861228866810978210821758693782612681388854980469e+00, // 0.750000
  2.69722457733621956421643517387565225362777709960938e+00, // 0.900000
  5.09511985013458890136917034396901726722717285156250e+00, // 0.990000
  7.40675477864855302811974979704245924949645996093750e+00, // 0.999000
  9.71024036697595960276885307393968105316162109375000e+00, // 0.999900
  };

  public static final double[] SCIPY_GLOGISTIC_QUANT_2_05 = { //
  -4.09511985013458978954759004409424960613250732421875e+00, // 0.000100
  -2.92174406546798248740515191457234323024749755859375e+00, // 0.001000
  -1.69722457733621956421643517387565225362777709960938e+00, // 0.010000
  -2.71162138430851129555776424240320920944213867187500e-01, // 0.100000
  5.00000000000000000000000000000000000000000000000000e-01, // 0.250000
  1.38137358701954271467116086569149047136306762695312e+00, // 0.500000
  2.36626404125887201956857097684405744075775146484375e+00, // 0.750000
  3.41705874790017549713638800312764942646026611328125e+00, // 0.900000
  5.79078277119367168523922373424284160137176513671875e+00, // 0.990000
  8.10015211557315417678637459175661206245422363281250e+00, // 0.999000
  1.04034125490967461757918499642983078956604003906250e+01, // 0.999900
  };

  public static final double[] SCIPY_GLOGISTIC_QUANT_05_05 = { //
  -1.79206807339523663813452003523707389831542968750000e+01, // 0.000100
  -1.33155095579637734459765852079726755619049072265625e+01, // 0.001000
  -8.71024036697584946864481025841087102890014648437500e+00, // 0.010000
  -4.09511985013458978954759004409424960613250732421875e+00, // 0.100000
  -2.20805020110221006390816000930499285459518432617188e+00, // 0.250000
  -5.98612288668109782108217586937826126813888549804688e-01, // 0.500000
  7.51314428280906221324642046965891495347023010253906e-01, // 0.750000
  1.95001017550599864414095918618841096758842468261719e+00, // 0.900000
  4.39693487554469086830977175850421190261840820312500e+00, // 0.990000
  6.71310722279666993728142188047058880329132080078125e+00, // 0.999000
  9.01704318266624582633994577918201684951782226562500e+00, // 0.999900
  };

  @Test
  public void testPDF() {
    checkPDF(new GeneralizedLogisticDistribution(.5, 1., 1.), P_CDFPDF, SCIPY_LOGISTIC_PDF_05, 1e-12);
    checkPDF(new GeneralizedLogisticDistribution(.5, 1., 1.), P_CDFPDF, GNUR_LOGISTIC_PDF_05, 1e-15);
    checkPDF(new GeneralizedLogisticDistribution(.5, 1., 2.), P_CDFPDF, SCIPY_GLOGISTIC_PDF_2_05, 1e-12);
    checkPDF(new GeneralizedLogisticDistribution(.5, 1., .5), P_CDFPDF, SCIPY_GLOGISTIC_PDF_05_05, 1e-12);
  }

  @Test
  public void testCDF() {
    checkCDF(new GeneralizedLogisticDistribution(.5, 1., 1.), P_CDFPDF, SCIPY_LOGISTIC_CDF_05, 1e-13);
    checkCDF(new GeneralizedLogisticDistribution(.5, 1., 1.), P_CDFPDF, GNUR_LOGISTIC_CDF_05, 0.);
    checkCDF(new GeneralizedLogisticDistribution(.5, 1., 2.), P_CDFPDF, SCIPY_GLOGISTIC_CDF_2_05, 1e-12);
    checkCDF(new GeneralizedLogisticDistribution(.5, 1., .5), P_CDFPDF, SCIPY_GLOGISTIC_CDF_05_05, 1e-13);
  }

  @Test
  public void testProbit() {
    checkQuantile(new GeneralizedLogisticDistribution(.5, 1., 1.), P_QUANT, SCIPY_LOGISTIC_QUANT_05, 0.);
    checkQuantile(new GeneralizedLogisticDistribution(.5, 1., 1.), P_QUANT, GNUR_LOGISTIC_QUANT_05, 1e-13);
    checkQuantile(new GeneralizedLogisticDistribution(.5, 1., 2.), P_QUANT, SCIPY_GLOGISTIC_QUANT_2_05, 1e-15);
    checkQuantile(new GeneralizedLogisticDistribution(.5, 1., .5), P_QUANT, SCIPY_GLOGISTIC_QUANT_05_05, 0.);
  }
}
