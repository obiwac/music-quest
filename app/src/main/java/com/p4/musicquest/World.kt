package com.p4.musicquest

import android.content.Context
import com.p4.musicquest.entities.Item
import com.p4.musicquest.entities.Monster
import com.p4.musicquest.entities.Player
import com.p4.musicquest.entities.Shoot
import com.p4.musicquest.entities.Villager

class World(context: Context, renderer: Renderer) {
    private var model: Model

    var player: Player? = null

    var discForest: Item? = null
    var discTest: Item? = null

    val listMonster = ArrayList<Monster>()
    val listCoordsMonster = arrayOf(arrayOf(-5f, 0f, 5f), arrayOf(-6f, 0f, 5f), arrayOf(-7f, 0f, 5f), arrayOf(-4f, 0f, 6f), arrayOf(-4f, 0f, 3f))

    val listItem = ArrayList<Item>()

    val listVillager = ArrayList<Villager>()
    val listCoordsVillager = arrayOf(arrayOf(-2f, 0f, 1f), arrayOf(-3f, 0f, 0.4f), arrayOf(-0.4f, 0f, 2f))

    val listShoot = ArrayList<Shoot>()

    var colliders: Array<Collider>

    init {
        model = Model(context, "map.ivx", "textures/map.ktx")

        player = Player(context, this, arrayOf(0f, 0f, -1f))

        for (i in listCoordsMonster.indices) {
            listMonster.add(Monster(context, this, listCoordsMonster[i], player))
        }

        for (i in listCoordsVillager.indices) {
            if (i == 0) {
                listVillager.add(Villager(context, player, this, listCoordsVillager[i], renderer))
                listVillager[0].showSignal = true
                listVillager[0].changeTextDialog(
                    "Bonjour mon brave\nBienvenue dans ce village,\nvous êtes nouveau, non ?\nJe ne vous " +
                        "avais jamais\nvu avant. Est-ce que vous\npouvez nous aider à nous\ndébarrasser des monstres en\nrécupérant " +
                        "tous les disques.\nJ'ai entendu dire que le premier\n disque ce situe pas\nloin d'ici dans la forêt\nqui jonche " +
                        "notre village")
            } else {
                listVillager.add(Villager(context, player, this, listCoordsVillager[i], renderer))
            }

        }

        discForest =  Item(context, "Disque de Forêt","textures/disc1.png", floatArrayOf(0f, 0f, 160f, 160f), floatArrayOf(160f, 160f), 0.5f, arrayOf(-5.75f, 0.2f, 5.2f), player, this, renderer, // dans truc violet -4.75f, 5.6f
            onClickInventory =  {
                println("item : ${discForest!!.name}")
            }, onClickScenario = {
                listVillager[0]!!.showSignal = true
                listVillager[0]!!.changeTextDialog("Super !\nVous avez pu récupérer\nle disque. Approcher\nle jukebox et cliquer\nsur le disque dans\nvotre inventaire pour\npouvoir accéder à de\nnouvelles zones")
            })

        discForest!!.textForDialog = "Vous avez récupéré :\nDisque de glace.\nRetourne dans le centre\nde la ville et va \nparler au vieux du village\n"
        discTest =  Item(context, "Disque de Test","textures/disc2.png", floatArrayOf(0f, 0f, 160f, 160f), floatArrayOf(160f, 160f), 0.5f, arrayOf(-1f, 0f, 15f), player, this, renderer
            , onClickInventory =  {
                println("item : ${discTest!!.name}")
            }, onClickScenario = {})


        colliders = arrayOf(
            Collider(-1.8299999713897703f, -0.15f, 0.9900000572204589f, -1.1700000286102294f, 0.7877474784851074f, 1.334999942779541f),
            Collider(-0.6984332084655761f, -0.217818546295166f, 0.1358904182910919f, -0.09047645330429077f, 0.6459843635559082f, 0.5808041810989379f),
            Collider(-2.713930320739746f, -0.217818546295166f, -0.41147997379302975f, -2.26681752204895f, 0.6459843635559082f, 0.1980842649936676f),
            Collider(-2.581460666656494f, -0.21781861782073975f, 1.9048182964324951f, -1.910591983795166f, 0.6459842920303345f, 2.375922632217407f),
            Collider(-0.06293106973171234f, -0.07972183227539062f, 1.9805070877075195f, 0.34440743923187256f, 0.4157973289489746f, 2.3544872760772706f),
            Collider(0.9941191434860229f, -0.07146130800247193f, 2.5792413711547852f, 1.401457643508911f, 0.4240578532218933f, 2.9532214164733888f),
            Collider(-1.9730987548828125f, -0.06929905414581299f, -0.8833334684371948f, -1.565760326385498f, 0.42622010707855223f, -0.5093534231185913f),
            Collider(-4.632361221313476f, -0.3336974859237671f, -0.09353407323360442f, -3.277659702301025f, 1.4925539016723632f, 1.1202537059783935f),
            Collider(-4.373495578765869f, -0.3336974859237671f, 1.4638591289520264f, -3.152760887145996f, 1.4925539016723632f, 2.82189245223999f),
            Collider(-1.4191054344177245f, -0.3336974859237671f, 2.851790142059326f, -0.06417359858751297f, 1.4925539016723632f, 4.128858947753906f),
            Collider(0.5071659922599793f, -0.3336974859237671f, 0.4702184200286865f, 1.8620977878570555f, 1.4925539016723632f, 1.7472872257232666f),
            Collider(2.013159942626953f, -0.3336974859237671f, 0.47897422313690186f, 3.368091773986816f, 1.4925539016723632f, 1.7560429573059082f),
            Collider(-1.3730700016021729f, -0.3336974859237671f, -2.268205976486206f, -0.1515170931816101f, 1.4925539016723632f, -0.9101698637008666f),
            Collider(-3.0043124198913573f, 0.1266620635986328f, -3.3386255264282227f, -2.7385708808898923f, 1.3773120403289794f, -3.041795539855957f),
            Collider(5.1823585510253904f, -0.06929905414581299f, 21.929340362548828f, 5.5896972656249995f, 0.42622010707855223f, 22.303321838378906f),
            Collider(-5.758261871337891f, -0.06929905414581299f, 19.834611511230467f, -5.350923156738281f, 0.42622010707855223f, 20.208592987060545f),
            Collider(-8.306195068359374f, -0.06929905414581299f, 16.691285705566404f, -7.898856353759765f, 0.42622010707855223f, 17.065266036987303f),
            Collider(-11.292631530761719f, -0.17901313304901123f, 17.074701690673827f, -9.111309242248534f, 1.1380406856536864f, 19.231809997558592f),
            Collider(-22.15154113769531f, -0.26262545585632324f, 29.8751220703125f, -19.97021942138672f, 1.0544282913208007f, 32.032232666015624f),
            Collider(-8.069328117370604f, -0.18003308773040771f, 20.597005462646482f, -5.888006401062012f, 1.13702073097229f, 22.75411605834961f),
            Collider(-4.465444278717041f, -0.23241283893585205f, 19.231102752685548f, -2.2841227054595947f, 1.0846409797668457f, 21.38821334838867f),
            Collider(2.9978364944458007f, -0.20543138980865477f, 19.264942932128907f, 5.1791582107543945f, 1.1116224288940428f, 21.42205352783203f),
            Collider(2.344219636917114f, -0.25008537769317624f, 13.696131134033203f, 4.52554121017456f, 1.0669684410095215f, 15.853239440917967f),
            Collider(1.4450396060943602f, -0.22698462009429932f, 9.93105354309082f, 3.6263611793518065f, 1.0900691986083983f, 12.088161849975586f),
            Collider(6.528723907470703f, -0.1710679292678833f, 16.527739334106446f, 8.710045623779296f, 1.1459858894348145f, 18.68484764099121f),
            Collider(10.235576248168945f, -0.17815139293670654f, 19.417570495605467f, 12.416897964477538f, 1.1389024257659912f, 21.574681091308594f),
            Collider(6.513593673706055f, -0.1793823480606079f, 22.488963317871093f, 8.694915390014648f, 1.1376714706420898f, 24.646073913574217f),
            Collider(6.940030288696289f, -0.7263532876968384f, 21.852889251708984f, 8.209667015075683f, 0.5907004952430724f, 23.269947052001953f),
            Collider(3.4886890411376954f, -0.7263532876968384f, 18.51488456726074f, 4.75832576751709f, 0.5907004952430724f, 19.93194122314453f),
            Collider(2.7697105407714844f, -0.7263532876968384f, 15.137864685058593f, 4.0393472671508786f, 0.5907004952430724f, 16.554920196533203f),
            Collider(5.800694274902344f, -0.7263532876968384f, 16.9675048828125f, 7.202134895324707f, 0.5907004952430724f, 18.21942901611328f),
            Collider(9.537807655334472f, -0.7263532876968384f, 19.842208099365234f, 10.939248275756835f, 0.5907004952430724f, 21.094129943847655f),
            Collider(0.6906259775161743f, -0.7263532876968384f, 10.367435073852539f, 2.0920663833618165f, 0.5907004952430724f, 11.61935920715332f),
            Collider(-6.794883155822753f, -0.7263532876968384f, 15.813268661499023f, -5.525246429443359f, 0.5907004952430724f, 17.23032417297363f),
            Collider(-9.810817337036132f, -0.7263532876968384f, 17.513645553588866f, -8.40937671661377f, 0.5907004952430724f, 18.76556968688965f),
            Collider(-3.984330368041992f, -0.7263532876968384f, 18.536671829223632f, -2.7146936416625977f, 0.5907004952430724f, 19.953728485107423f),
            Collider(-7.540005683898926f, -0.7263532876968384f, 19.870049285888673f, -6.270368957519531f, 0.5907004952430724f, 21.28710708618164f),
            Collider(-20.71314239501953f, -0.7263532876968384f, 30.35068130493164f, -19.311703491210938f, 0.5907004952430724f, 31.602603149414062f),
            Collider(-7.281062507629394f, -0.7823065280914306f, 14.338819885253907f, -5.099740791320801f, 1.1147575378417969f, 16.49592819213867f),
            Collider(0.1198749303817749f, -0.4401380181312561f, 27.745317077636717f, 0.848035454750061f, 0.8170032978057861f, 28.50175323486328f),
            Collider(-1.8046643257141113f, -0.4401380181312561f, 28.816522979736327f, -1.0765037298202513f, 0.8170032978057861f, 29.57295913696289f),
            Collider(-0.49743010997772213f, -0.4401380181312561f, 29.99666519165039f, 0.23073041439056396f, 0.8170032978057861f, 30.753101348876953f),
            Collider(-1.877288246154785f, -0.4401380181312561f, 31.75779876708984f, -1.1491277933120727f, 0.8170032978057861f, 32.514234924316405f),
            Collider(0.646399712562561f, -0.4401380181312561f, 32.356947326660155f, 1.3745601654052735f, 0.8170032978057861f, 33.113383483886714f),
            Collider(-2.410414695739746f, 0.1266620635986328f, -4.452183437347412f, -2.1446734428405763f, 1.3773120403289794f, -4.155353450775146f),
            Collider(-3.4348883628845215f, 0.1266620635986328f, -4.882759094238281f, -3.1691468238830565f, 1.3773120403289794f, -4.585929107666016f),
            Collider(-4.548446273803711f, 0.1266620635986328f, -5.046080589294434f, -4.282704734802246f, 1.3773120403289794f, -4.749250888824463f),
            Collider(-5.572919082641602f, 0.1266620635986328f, -5.239097213745117f, -5.307178115844726f, 1.3773120403289794f, -4.942267227172851f),
            Collider(-6.22620620727539f, 0.1266620635986328f, -6.174485778808593f, -5.9604652404785154f, 1.3773120403289794f, -5.877655792236328f),
            Collider(-4.786004734039307f, 0.1266620635986328f, -6.441739654541015f, -4.520263195037842f, 1.3773120403289794f, -6.14490966796875f),
            Collider(-3.6130568504333493f, 0.1266620635986328f, -6.100248527526856f, -3.3473153114318848f, 1.3773120403289794f, -5.803418540954589f),
            Collider(-2.454956531524658f, 0.1266620635986328f, -5.98146915435791f, -2.1892151355743406f, 1.3773120403289794f, -5.684639167785645f),
            Collider(-1.4304830074310302f, 0.1266620635986328f, -5.446961402893066f, -1.1647416830062867f, 1.3773120403289794f, -5.150131416320801f),
            Collider(-0.7178059816360474f, 0.1266620635986328f, -4.689742183685302f, -0.45206458568572994f, 1.3773120403289794f, -4.392912197113037f),
            Collider(-1.4601778507232666f, 0.1266620635986328f, -3.7692008972167965f, -1.1944365262985228f, 1.3773120403289794f, -3.472370910644531f),
            Collider(-0.04967113137245178f, 0.1266620635986328f, -3.501947021484375f, 0.21607022881507873f, 1.3773120403289794f, -3.205117034912109f),
            Collider(0.633311104774475f, 0.1266620635986328f, -4.556115245819091f, 0.8990525007247925f, 1.3773120403289794f, -4.259285259246826f),
            Collider(0.14334563612937926f, 0.1266620635986328f, -5.862689781188965f, 0.40908701419830323f, 1.3773120403289794f, -5.565859794616699f),
            Collider(-0.10906083583831787f, 0.1266620635986328f, -7.2138067245483395f, 0.15668052434921265f, 1.3773120403289794f, -6.916976737976074f),
            Collider(-0.8514327764511108f, 0.1266620635986328f, -6.426892662048339f, -0.5856913805007934f, 1.3773120403289794f, -6.130062675476074f),
            Collider(-1.8313636779785156f, 0.1266620635986328f, -7.184112167358398f, -1.5656224250793456f, 1.3773120403289794f, -6.887282180786133f),
            Collider(-3.0637012481689454f, 0.1266620635986328f, -7.065332794189453f, -2.7979597091674804f, 1.3773120403289794f, -6.768502807617187f),
            Collider(-7.265526580810547f, 0.1266620635986328f, -5.2984874725341795f, -6.999785614013672f, 1.3773120403289794f, -5.001657485961914f),
            Collider(-6.43406982421875f, 0.1266620635986328f, -4.85306453704834f, -6.168328857421875f, 1.3773120403289794f, -4.556234550476074f),
            Collider(-5.468986129760742f, 0.1266620635986328f, -3.9473708152770994f, -5.203245162963867f, 1.3773120403289794f, -3.650540828704834f),
            Collider(-4.0584797859191895f, 0.1266620635986328f, -3.8137438774108885f, -3.7927382469177244f, 1.3773120403289794f, -3.516913890838623f),
            Collider(-4.325733661651611f, 0.1266620635986328f, -2.6259487152099608f, -4.059992122650146f, 1.3773120403289794f, -2.3291187286376953f),
            Collider(0.8411754369735718f, 0.1266620635986328f, -7.020790672302246f, 1.1069168329238892f, 1.3773120403289794f, -6.72396068572998f),
            Collider(1.6874795436859131f, 0.1266620635986328f, -6.486282920837402f, 1.953220796585083f, 1.3773120403289794f, -6.1894529342651365f),
            Collider(2.6080206871032714f, 0.1266620635986328f, -6.768384361267089f, 2.8737622261047364f, 1.3773120403289794f, -6.471554374694824f),
            Collider(1.7914116382598877f, 0.1266620635986328f, -5.446962547302246f, 2.0571528911590575f, 1.3773120403289794f, -5.15013256072998f),
            Collider(1.6726321220397948f, 0.1266620635986328f, -3.858287143707275f, 1.9383733749389647f, 1.3773120403289794f, -3.5614571571350098f),
            Collider(1.0638870477676392f, 0.1266620635986328f, -2.5814074516296386f, 1.3296283721923827f, 1.3773120403289794f, -2.284577465057373f),
            Collider(2.2368348598480225f, 0.1266620635986328f, -2.7892716407775877f, 2.5025762557983398f, 1.3773120403289794f, -2.4924416542053223f),
            Collider(2.9198169708251953f, 0.1266620635986328f, -4.585811805725098f, 3.18555850982666f, 1.3773120403289794f, -4.2889818191528315f),
            Collider(3.1276811599731444f, 0.1266620635986328f, -5.699369430541992f, 3.3934226989746095f, 1.3773120403289794f, -5.402539443969727f),
            Collider(3.7661210060119625f, 0.1266620635986328f, -6.501131057739258f, 4.031862545013428f, 1.3773120403289794f, -6.204301071166992f),
            Collider(5.013306427001953f, 0.1266620635986328f, -6.011165428161621f, 5.279047393798828f, 1.3773120403289794f, -5.7143354415893555f),
            Collider(4.38971357345581f, 0.1266620635986328f, -4.942149925231933f, 4.655455112457275f, 1.3773120403289794f, -4.645320224761963f),
            Collider(5.755678367614746f, 0.1266620635986328f, -5.060929298400879f, 6.021419334411621f, 1.3773120403289794f, -4.764099597930908f),
            Collider(6.8989311218261715f, 0.1266620635986328f, -4.763980865478516f, 7.164672088623047f, 1.3773120403289794f, -4.46715087890625f),
            Collider(6.275338554382324f, 0.1266620635986328f, -3.784049892425537f, 6.541079521179199f, 1.3773120403289794f, -3.4872199058532716f),
            Collider(4.701509571075439f, 0.1266620635986328f, -3.769202327728271f, 4.967250823974609f, 1.3773120403289794f, -3.4723723411560057f),
            Collider(3.454324436187744f, 0.1266620635986328f, -3.353473949432373f, 3.720065975189209f, 1.3773120403289794f, -3.0566439628601074f),
            Collider(2.667410087585449f, 0.1266620635986328f, -1.6460183143615723f, 2.933151626586914f, 1.3773120403289794f, -1.349188470840454f),
            Collider(4.226391220092773f, 0.1266620635986328f, -1.9875094413757324f, 4.492132759094238f, 1.3773120403289794f, -1.6906795978546143f),
            Collider(5.5032714843749995f, 0.1266620635986328f, -2.8041186332702637f, 5.769012451171875f, 1.3773120403289794f, -2.507288646697998f),
            Collider(7.374048614501953f, 0.1266620635986328f, -3.2643891334533692f, 7.639789581298828f, 1.3773120403289794f, -2.9675591468811033f),
            Collider(8.086725997924804f, 0.1266620635986328f, -2.2102207660675046f, 8.35246696472168f, 1.3773120403289794f, -1.9133909225463865f),
            Collider(6.6762193679809565f, 0.1266620635986328f, -2.1953733444213865f, 6.941960334777832f, 1.3773120403289794f, -1.8985435009002685f),
            Collider(5.904152297973632f, 0.1266620635986328f, -1.4233065605163573f, 6.169893264770508f, 1.3773120403289794f, -1.1264767169952392f),
            Collider(4.285781192779541f, 0.1266620635986328f, -0.6957820415496826f, 4.551522731781006f, 1.3773120403289794f, -0.39895216226577757f),
            Collider(6.928626251220703f, 0.1266620635986328f, -0.9630359172821045f, 7.194367218017578f, 1.3773120403289794f, -0.6662060737609863f),
            Collider(8.160963821411132f, 0.1266620635986328f, -1.0966628551483153f, 8.426704788208008f, 1.3773120403289794f, -0.7998330116271972f),
            Collider(8.1312686920166f, 0.1266620635986328f, 0.13567453622817993f, 8.397009658813475f, 1.3773120403289794f, 0.4325044512748718f),
            Collider(7.047405624389648f, 0.1266620635986328f, 0.18021683692932128f, 7.313146591186523f, 1.3773120403289794f, 0.47704675197601315f),
            Collider(5.696288681030273f, 0.1266620635986328f, 0.06143732070922851f, 5.962029647827149f, 1.3773120403289794f, 0.3582672357559204f),
            Collider(4.7163571357727045f, 0.1266620635986328f, 0.9671310424804687f, 4.982098388671875f, 1.3773120403289794f, 1.263960886001587f),
            Collider(6.468355178833008f, 0.1266620635986328f, 1.1007579803466796f, 6.7340961456298825f, 1.3773120403289794f, 1.397587823867798f),
            Collider(7.774930000305176f, 0.1266620635986328f, 1.189842653274536f, 8.04067096710205f, 1.3773120403289794f, 1.4866724967956542f),
            Collider(5.577508735656738f, 0.1266620635986328f, 1.8579773426055908f, 5.843249702453613f, 1.3773120403289794f, 2.154807186126709f),
            Collider(6.928625679016113f, 0.1266620635986328f, 2.199468469619751f, 7.1943666458129885f, 1.3773120403289794f, 2.496298313140869f),
            Collider(7.834319686889648f, 0.1266620635986328f, 2.615196704864502f, 8.100060653686523f, 1.3773120403289794f, 2.9120266914367674f),
            Collider(7.181032562255859f, 0.1266620635986328f, 3.773297023773193f, 7.446773529052734f, 1.3773120403289794f, 4.070127010345459f),
            Collider(6.275338554382324f, 0.1266620635986328f, 2.9863829612731934f, 6.541079521179199f, 1.3773120403289794f, 3.283212947845459f),
            Collider(4.805441665649414f, 0.1266620635986328f, 3.0903150558471677f, 5.0711826324462885f, 1.3773120403289794f, 3.3871450424194336f),
            Collider(3.7364258766174316f, 0.1266620635986328f, 2.273706007003784f, 4.002167415618897f, 1.3773120403289794f, 2.570535850524902f),
            Collider(3.662188625335693f, 0.1266620635986328f, 3.639670085906982f, 3.927930164337158f, 1.3773120403289794f, 3.936500072479248f),
            Collider(4.849984359741211f, 0.1266620635986328f, 4.3968895912170405f, 5.115725326538086f, 1.3773120403289794f, 4.693719577789307f),
            Collider(6.141711616516113f, 0.1266620635986328f, 4.411737155914307f, 6.407452583312988f, 1.3773120403289794f, 4.708567142486572f),
            Collider(5.84476261138916f, 0.1266620635986328f, 5.4956005096435545f, 6.110503578186035f, 1.3773120403289794f, 5.79243049621582f),
            Collider(4.59757719039917f, 0.1266620635986328f, 5.51044807434082f, 4.86331844329834f, 1.3773120403289794f, 5.807278060913085f),
            Collider(3.795815563201904f, 0.1266620635986328f, 5.1392618179321286f, 4.061557102203369f, 1.3773120403289794f, 5.436091804504394f),
            Collider(2.221987009048462f, 0.1266620635986328f, 3.8029921531677244f, 2.487728404998779f, 1.3773120403289794f, 4.09982213973999f),
            Collider(2.504088306427002f, 0.1266620635986328f, 5.124414253234863f, 2.769829845428467f, 1.3773120403289794f, 5.4212442398071286f),
            Collider(1.2420560359954833f, 0.1266620635986328f, 5.079872131347656f, 1.5077972888946534f, 1.3773120403289794f, 5.376702117919922f),
            Collider(2.3704615116119383f, 0.1266620635986328f, 6.163735198974609f, 2.636202907562256f, 1.3773120403289794f, 6.460565185546875f),
            Collider(3.3503923416137695f, 0.1266620635986328f, 6.208277320861816f, 3.616133880615234f, 1.3773120403289794f, 6.5051073074340815f),
            Collider(4.59757719039917f, 0.1266620635986328f, 6.4606836318969725f, 4.86331844329834f, 1.3773120403289794f, 6.757513618469238f),
            Collider(2.9495112419128415f, 0.1266620635986328f, 7.232750129699706f, 3.2152527809143066f, 1.3773120403289794f, 7.529580116271973f),
            Collider(1.5538520336151123f, 0.1266620635986328f, 7.589088821411132f, 1.819593286514282f, 1.3773120403289794f, 7.885918807983398f),
            Collider(1.3311404228210448f, 0.1266620635986328f, 6.208277320861816f, 1.5968816757202149f, 1.3773120403289794f, 6.5051073074340815f),
            Collider(0.38090422153472897f, 0.1266620635986328f, 6.950649261474609f, 0.6466456174850463f, 1.3773120403289794f, 7.247479248046875f),
            Collider(-0.01997660994529724f, 0.1266620635986328f, 7.811800575256347f, 0.24576475024223327f, 1.3773120403289794f, 8.108630561828614f),
            Collider(-0.6584164381027221f, 0.1266620635986328f, 6.9061071395874025f, -0.3926751136779785f, 1.3773120403289794f, 7.202937126159668f),
            Collider(-0.16845096945762633f, 0.1266620635986328f, 5.926175880432129f, 0.09729039072990417f, 1.3773120403289794f, 6.223005867004394f),
            Collider(-1.3413986206054687f, 0.1266620635986328f, 7.811800575256347f, -1.0756572961807251f, 1.3773120403289794f, 8.108630561828614f),
            Collider(-2.454956817626953f, 0.1266620635986328f, 7.08427619934082f, -2.1892154216766357f, 1.3773120403289794f, 7.381106185913086f),
            Collider(-1.4304832935333252f, 0.1266620635986328f, 6.371599388122559f, -1.1647419691085814f, 1.3773120403289794f, 6.668429374694824f),
            Collider(-2.4846516609191895f, 0.1266620635986328f, 5.6143798828125f, -2.218910264968872f, 1.3773120403289794f, 5.9112098693847654f),
            Collider(-1.7571269989013671f, 0.1266620635986328f, 4.782922840118408f, -1.4913857460021973f, 1.3773120403289794f, 5.079753112792969f),
            Collider(-3.108244228363037f, 0.1266620635986328f, 7.960275077819824f, -2.8425026893615724f, 1.3773120403289794f, 8.25710506439209f),
            Collider(-3.3309556961059568f, 0.1266620635986328f, 6.802174758911133f, -3.065214157104492f, 1.3773120403289794f, 7.099004745483398f),
            Collider(-2.8855324745178224f, 0.1266620635986328f, 4.114788150787353f, -2.6197909355163573f, 1.3773120403289794f, 4.411618137359619f),
            Collider(-3.9545479774475094f, 0.1266620635986328f, 4.589906215667725f, -3.688806438446045f, 1.3773120403289794f, 4.886736488342285f),
            Collider(-3.9693955421447753f, 0.1266620635986328f, 5.926175880432129f, -3.7036540031433103f, 1.3773120403289794f, 6.223005867004394f),
            Collider(-4.979021072387695f, 0.1266620635986328f, 6.817022323608398f, -4.713279819488525f, 1.3773120403289794f, 7.113852310180664f),
            Collider(-4.266344261169434f, 0.1266620635986328f, 7.737563323974609f, -4.000602722167969f, 1.3773120403289794f, 8.034393310546875f),
            Collider(-5.8847150802612305f, 0.1266620635986328f, 7.5148515701293945f, -5.618974113464355f, 1.3773120403289794f, 7.81168155670166f),
            Collider(-7.027967834472656f, 0.1266620635986328f, 6.861564445495605f, -6.762226867675781f, 1.3773120403289794f, 7.158394432067871f),
            Collider(-6.003494453430176f, 0.1266620635986328f, 6.282514572143555f, -5.7377534866333f, 1.3773120403289794f, 6.57934455871582f),
            Collider(-6.879493331909179f, 0.1266620635986328f, 5.762854385375976f, -6.613752365112305f, 1.3773120403289794f, 6.059684371948242f),
            Collider(-7.963356399536132f, 0.1266620635986328f, 6.089497947692871f, -7.697615432739258f, 1.3773120403289794f, 6.386327934265137f),
            Collider(-8.735423469543457f, 0.1266620635986328f, 4.946245193481445f, -8.469682502746581f, 1.3773120403289794f, 5.243075180053711f),
            Collider(-7.35461196899414f, 0.1266620635986328f, 4.797770404815673f, -7.088871002197266f, 1.3773120403289794f, 5.094600677490234f),
            Collider(-6.241053771972656f, 0.1266620635986328f, 4.738380718231201f, -5.975312805175781f, 1.3773120403289794f, 5.035210990905761f),
            Collider(-5.172038269042969f, 0.1266620635986328f, 3.788144588470459f, -4.906297302246093f, 1.3773120403289794f, 4.084974575042724f),
            Collider(-6.389528274536133f, 0.1266620635986328f, 3.446653461456299f, -6.123787307739257f, 1.3773120403289794f, 3.7434834480285644f),
            Collider(-7.503086471557617f, 0.1266620635986328f, 2.9715353965759275f, -7.237345504760742f, 1.3773120403289794f, 3.2683653831481934f),
            Collider(-7.740645217895508f, 0.1266620635986328f, 3.832686710357666f, -7.474904251098632f, 1.3773120403289794f, 4.129516696929931f),
            Collider(-9.091762161254882f, 0.1266620635986328f, 3.58028039932251f, -8.826021194458008f, 1.3773120403289794f, 3.8771103858947753f),
            Collider(-8.631491661071777f, 0.1266620635986328f, 2.659739112854004f, -8.365750694274903f, 1.3773120403289794f, 2.9565690994262694f),
            Collider(-9.477795410156249f, 0.1266620635986328f, 1.9025198936462402f, -9.212054443359374f, 1.3773120403289794f, 2.1993497371673585f),
            Collider(-8.319695091247558f, 0.1266620635986328f, 1.5016390800476074f, -8.053954124450684f, 1.3773120403289794f, 1.7984689235687255f),
            Collider(-7.0725099563598635f, 0.1266620635986328f, 1.7688929557800293f, -6.806768989562988f, 1.3773120403289794f, 2.0657227993011475f),
            Collider(-5.98864688873291f, 0.1266620635986328f, 1.4867916584014893f, -5.722905921936035f, 1.3773120403289794f, 1.7836215019226074f),
            Collider(-7.176442337036133f, 0.1266620635986328f, 0.47716580629348754f, -6.9107013702392575f, 1.3773120403289794f, 0.7739956855773925f),
            Collider(-8.156373596191406f, 0.1266620635986328f, 0.0020477771759033203f, -7.890632629394531f, 1.3773120403289794f, 0.2988776922225952f),
            Collider(-6.359833145141601f, 0.1266620635986328f, -0.3691381931304932f, -6.094092178344726f, 1.3773120403289794f, -0.07230828702449799f),
            Collider(-7.369458961486816f, 0.1266620635986328f, -0.7848664283752441f, -7.103717994689942f, 1.3773120403289794f, -0.4880365490913391f),
            Collider(-7.80003490447998f, 0.1266620635986328f, -1.8241870880126951f, -7.534293937683105f, 1.3773120403289794f, -1.527357244491577f),
            Collider(-6.716171836853027f, 0.1266620635986328f, -1.527238368988037f, -6.450430870056152f, 1.3773120403289794f, -1.2304085254669188f),
            Collider(-5.142343139648437f, 0.1266620635986328f, -1.5123909473419188f, -4.8766021728515625f, 1.3773120403289794f, -1.2155611038208007f),
            Collider(-7.0576629638671875f, 0.1266620635986328f, -2.5962541580200194f, -6.791921997070312f, 1.3773120403289794f, -2.299424171447754f),
            Collider(-5.676850891113281f, 0.1266620635986328f, -2.477474498748779f, -5.411109924316406f, 1.3773120403289794f, -2.180644655227661f),
            Collider(-8.319695663452148f, 0.1266620635986328f, -3.101067066192627f, -8.053954696655273f, 1.3773120403289794f, -2.804237079620361f),
            Collider(-9.032373046875f, 0.1266620635986328f, -2.002356433868408f, -8.766632080078125f, 1.3773120403289794f, -1.70552659034729f),
            Collider(-9.106610298156738f, 0.1266620635986328f, -0.7254767417907715f, -8.840869331359864f, 1.3773120403289794f, -0.42864686250686646f),
            Collider(-9.388711738586425f, 0.1266620635986328f, 0.6256402015686034f, -9.12297077178955f, 1.3773120403289794f, 0.9224700450897216f),
            Collider(-6.701324844360351f, 0.1266620635986328f, -3.650422382354736f, -6.435583877563476f, 1.3773120403289794f, -3.3535923957824707f),
            Collider(-7.859425163269043f, 0.1266620635986328f, -4.333404636383056f, -7.593684196472168f, 1.3773120403289794f, -4.036574649810791f),
            Collider(0.6877515077590942f, 0.1266620635986328f, 9.222307777404785f, 0.9534929037094115f, 1.3773120403289794f, 9.519137763977051f),
            Collider(4.767115688323974f, 0.1266620635986328f, 8.940087890625f, 5.0328569412231445f, 1.3773120403289794f, 9.236917877197266f),
            Collider(6.229529571533203f, 0.1266620635986328f, 11.50572509765625f, 6.495270538330078f, 1.3773120403289794f, 11.802555084228516f),
            Collider(3.4842967987060547f, 0.1266620635986328f, 12.147134399414062f, 3.7500383377075193f, 1.3773120403289794f, 12.443964385986328f),
            Collider(0.3542187452316284f, 0.1266620635986328f, 12.531979751586913f, 0.6199601411819458f, 1.3773120403289794f, 12.82880973815918f),
            Collider(-3.237674331665039f, 0.1266620635986328f, 10.145936965942383f, -2.971932792663574f, 1.3773120403289794f, 10.442766952514647f),
            Collider(-5.880281066894531f, 0.1266620635986328f, 9.786748123168945f, -5.614540100097656f, 1.3773120403289794f, 10.083578109741211f),
            Collider(-6.008563041687012f, 0.1266620635986328f, 12.660262298583984f, -5.742822074890136f, 1.3773120403289794f, 12.95709228515625f),
            Collider(-8.368949890136719f, 0.1266620635986328f, 13.686516952514648f, -8.103208923339844f, 1.3773120403289794f, 13.983346939086914f),
            Collider(-3.545551013946533f, 0.1266620635986328f, 14.19964485168457f, -3.2798094749450684f, 1.3773120403289794f, 14.496474838256836f),
            Collider(5.074992370605469f, 0.1266620635986328f, 16.175185775756834f, 5.340733337402344f, 1.3773120403289794f, 16.4720157623291f),
            Collider(7.486691665649413f, 0.1266620635986328f, 15.354182052612304f, 7.752432632446289f, 1.3773120403289794f, 15.65101203918457f),
            Collider(9.872734451293946f, 0.1266620635986328f, 15.533776473999023f, 10.13847541809082f, 1.3773120403289794f, 15.830606460571289f),
            Collider(11.694337463378906f, 0.1266620635986328f, 17.63759994506836f, 11.96007843017578f, 1.3773120403289794f, 17.934429931640626f),
            Collider(8.435977363586426f, 0.1266620635986328f, 19.176982498168943f, 8.7017183303833f, 1.3773120403289794f, 19.47381362915039f),
            Collider(5.716401100158691f, 0.1266620635986328f, 21.434745025634765f, 5.982142066955566f, 1.3773120403289794f, 21.73157501220703f),
            Collider(3.7408598899841308f, 0.1266620635986328f, 23.71816177368164f, 4.006601428985595f, 1.3773120403289794f, 24.014991760253906f),
            Collider(1.9705698966979979f, 0.1266620635986328f, 22.02484130859375f, 2.2363111495971677f, 1.3773120403289794f, 22.321671295166016f),
            Collider(2.252790069580078f, 0.1266620635986328f, 19.100011825561523f, 2.518531322479248f, 1.3773120403289794f, 19.39684295654297f),
            Collider(9.539200973510741f, 0.1266620635986328f, 22.384030151367188f, 9.804941940307616f, 1.3773120403289794f, 22.68086013793945f),
            Collider(7.871536445617675f, 0.1266620635986328f, 25.821984100341798f, 8.13727741241455f, 1.3773120403289794f, 26.11881408691406f),
            Collider(12.643622589111327f, 0.1266620635986328f, 23.15372085571289f, 12.909363555908202f, 1.3773120403289794f, 23.450550842285157f),
            Collider(13.695533752441406f, 0.1266620635986328f, 20.562425994873045f, 13.961274719238281f, 1.3773120403289794f, 20.859255981445312f),
            Collider(15.337542343139647f, 0.1266620635986328f, 22.076152038574218f, 15.603283309936522f, 1.3773120403289794f, 22.372982025146484f),
            Collider(16.54339256286621f, 0.1266620635986328f, 25.077948760986327f, 16.809133529663086f, 1.3773120403289794f, 25.374778747558594f),
            Collider(14.157349777221679f, 0.1266620635986328f, 27.566617584228513f, 14.423090744018554f, 1.3773120403289794f, 27.86344757080078f),
            Collider(11.181209564208984f, 0.1266620635986328f, 26.642987823486326f, 11.446950531005859f, 1.3773120403289794f, 26.939817810058592f),
            Collider(10.231923294067382f, 0.1266620635986328f, 29.439532470703124f, 10.497664260864257f, 1.3773120403289794f, 29.73636245727539f),
            Collider(13.46462631225586f, 0.1266620635986328f, 31.03022689819336f, 13.730367279052734f, 1.3773120403289794f, 31.327056884765625f),
            Collider(16.59470443725586f, 0.1266620635986328f, 32.518297576904295f, 16.860445404052733f, 1.3773120403289794f, 32.81512756347656f),
            Collider(16.876924896240233f, 0.1266620635986328f, 29.259938049316403f, 17.14266586303711f, 1.3773120403289794f, 29.55676803588867f),
            Collider(18.98074836730957f, 0.1266620635986328f, 26.54036178588867f, 19.246490478515625f, 1.3773120403289794f, 26.837191772460937f),
            Collider(21.28982162475586f, 0.1266620635986328f, 28.36196365356445f, 21.555562591552732f, 1.3773120403289794f, 28.658793640136718f),
            Collider(19.160341644287108f, 0.1266620635986328f, 30.927600860595703f, 19.426082611083984f, 1.3773120403289794f, 31.224430847167966f),
            Collider(23.444956970214843f, 0.1266620635986328f, 29.901345062255857f, 23.71069793701172f, 1.3773120403289794f, 30.198175048828123f),
            Collider(22.085169982910156f, 0.1266620635986328f, 32.18476181030273f, 22.350910949707032f, 1.3773120403289794f, 32.481591796875f),
            Collider(25.00999603271484f, 0.1266620635986328f, 32.9287971496582f, 25.275736999511718f, 1.3773120403289794f, 33.225627136230464f),
            Collider(-5.136248016357421f, 0.1266620635986328f, 20.228890228271485f, -4.870507049560547f, 1.3773120403289794f, 20.525720214843748f),
            Collider(-9.343893814086913f, 0.1266620635986328f, 20.33151626586914f, -9.078152847290038f, 1.3773120403289794f, 20.628346252441407f),
            Collider(-13.192350769042969f, 0.1266620635986328f, 18.89475746154785f, -12.926609802246093f, 1.3773120403289794f, 19.191587448120117f),
            Collider(-15.065266799926757f, 0.1266620635986328f, 21.33211441040039f, -14.799525833129882f, 1.3773120403289794f, 21.628944396972656f),
            Collider(-14.244263076782227f, 0.1266620635986328f, 25.308853912353516f, -13.97852210998535f, 1.3773120403289794f, 25.60568389892578f),
            Collider(-10.780652618408203f, 0.1266620635986328f, 26.41207809448242f, -10.514911651611328f, 1.3773120403289794f, 26.708908081054688f),
            Collider(-18.323627471923828f, 0.1266620635986328f, 24.410881805419923f, -18.05788650512695f, 1.3773120403289794f, 24.707711791992185f),
            Collider(-17.502623748779296f, 0.1266620635986328f, 27.59227294921875f, -17.23688278198242f, 1.3773120403289794f, 27.889102935791016f),
            Collider(-13.70547981262207f, 0.1266620635986328f, 30.260536193847656f, -13.439738845825195f, 1.3773120403289794f, 30.55736618041992f),
            Collider(-17.861812591552734f, 0.1266620635986328f, 32.21042175292968f, -17.596071624755858f, 1.3773120403289794f, 32.50725173950195f),
            Collider(-20.504420471191406f, 0.1266620635986328f, 27.771867370605467f, -20.23867950439453f, 1.3773120403289794f, 28.068697357177733f),
            Collider(-23.891061401367185f, 0.1266620635986328f, 28.438932037353513f, -23.625320434570312f, 1.3773120403289794f, 28.73576202392578f),
            Collider(-24.019342803955077f, 0.1266620635986328f, 32.13345108032227f, -23.753601837158204f, 1.3773120403289794f, 32.43028106689453f),
            Collider(-27.996082305908203f, 0.1266620635986328f, 31.54335479736328f, -27.730341339111327f, 1.3773120403289794f, 31.840184783935545f),
            Collider(-31.703668212890623f, -0.5192976951599121f, 34.127760314941405f, -13.272418212890624f, 3.615629196166992f, 36.58533554077148f),
            Collider(-13.946511840820312f, -0.5192976951599121f, 32.71666030883789f, -10.589996337890625f, 3.615629196166992f, 35.174235534667964f),
            Collider(-11.423634338378905f, -0.5192976951599121f, 30.877954101562498f, -8.067118835449218f, 3.615629196166992f, 33.33552932739258f),
            Collider(-9.306983184814452f, -0.5192976951599121f, 25.55105667114258f, -5.950467681884765f, 3.615629196166992f, 33.61264572143555f),
            Collider(-8.136405944824219f, -0.5522403717041016f, 25.07292022705078f, -2.177661895751953f, 3.582686519622803f, 27.44438323974609f),
            Collider(-4.1456986427307125f, -0.5522403717041016f, 25.276025390624998f, -2.319912815093994f, 3.582686519622803f, 32.659542846679685f),
            Collider(-5.535419082641601f, -0.5522403717041016f, 31.592143249511718f, -3.7096332550048827f, 3.582686519622803f, 34.95023803710937f),
            Collider(-4.526903343200684f, -0.5522403717041016f, 34.338226318359375f, 3.8767364501953123f, 3.582686519622803f, 35.806871795654295f),
            Collider(2.6303483963012693f, -0.5522403717041016f, 31.592143249511718f, 4.456134223937988f, 3.582686519622803f, 34.95023803710937f),
            Collider(1.5041407585144042f, -0.5522403717041016f, 25.276025390624998f, 3.329926586151123f, 3.582686519622803f, 32.659542846679685f),
            Collider(0.8433265686035156f, -0.5522403717041016f, 25.175546264648435f, 6.802070617675781f, 3.582686519622803f, 27.54700927734375f),
            Collider(4.136959362030029f, -0.5192976951599121f, 26.782562255859375f, 7.4934745788574215f, 3.615629196166992f, 34.84415130615234f),
            Collider(5.830280685424804f, -0.5192976951599121f, 29.27123107910156f, 9.186795043945311f, 3.615629196166992f, 37.33282012939453f),
            Collider(10.060977172851562f, -0.5192976951599121f, 34.07972259521484f, 30.633000183105466f, 3.615629196166992f, 37.501382446289064f),
            Collider(8.34861946105957f, -0.5192976951599121f, 31.647498321533202f, 11.963933944702148f, 3.615629196166992f, 35.06915588378906f),
        )
    }

    fun draw(shader: Shader) {
        model.draw(shader)
    }
}