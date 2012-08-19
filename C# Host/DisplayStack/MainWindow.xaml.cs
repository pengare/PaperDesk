using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;
using System.Windows.Threading;
using System.IO.Ports;
using System.IO;
using System.Diagnostics;
using System.Threading;
using System.Linq;
using System.Net.Sockets;
using System.Net;
using System.Media;

namespace DisplayStack
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        static string HostIP = "130.15.33.34";
        enum DisplayMode
        {
            Initial,
            Pile,
            Stack,
            Linear,
            Fan
        }
        DisplayMode displayMode = DisplayMode.Initial;
        Boolean modeChanged = true;
        
        //index used to indicate which images shown on device1
        int pileIndex = 0;

        // Timing
        DispatcherTimer mainTimer = new DispatcherTimer();
        int timerStep = 50;
        int readCounter = 0;

        //eInk Socket
        static TcpListener eink = new TcpListener(IPAddress.Parse(HostIP), 2222);
        //Socket communicate with device 1
        Socket soc;
        NetworkStream s;
        StreamReader sr;
        StreamWriter sw;
        ////Socket communicate with device 2
        //Socket soc2;
        //NetworkStream s2;
        //StreamReader sr2;
        //StreamWriter sw2;
        ////Socket communicate with device 3
        //Socket soc3;
        //NetworkStream s3;
        //StreamReader sr3;
        //StreamWriter sw3;

        public MainWindow()
        {
            InitializeComponent();
            SetupAndroid();
            SetupArduino();

            this.KeyDown += new KeyEventHandler(KeyDownEventHandler);
            CompositionTarget.Rendering += MainLoopToReadArduino;

            mainTimer.Tick += new EventHandler(MainLoopToReadArduino);
            mainTimer.Interval = new TimeSpan(0, 0, 0, 0, timerStep);
            mainTimer.Start();
        }

        public void SetupAndroid()
        {
            eink.Start();
            Thread t = new Thread(new ThreadStart(EInkSender));
            t.Start();
        }

        public void SetupArduino()
        {

        }

        public void EInkSender()
        {
            while (true)
            {
                //Wait connection from 3 device
                soc = eink.AcceptSocket();
                //soc2 = eink.AcceptSocket();
                //soc3 = eink.AcceptSocket();

                try
                {
                    s = new NetworkStream(soc);
                    sr = new StreamReader(s);
                    sw = new StreamWriter(s);
                    sw.AutoFlush = true;

                    //s2 = new NetworkStream(soc2);
                    //sr2 = new StreamReader(s2);
                    //sw2 = new StreamWriter(s2);
                    //sw2.AutoFlush = true;

                    //s3 = new NetworkStream(soc3);
                    //sr3 = new StreamReader(s3);
                    //sw3 = new StreamWriter(s3);
                    //sw3.AutoFlush = true;

                    Console.WriteLine("Connect to flexible display");
                    while (true)
                    {
                        if (modeChanged)
                        {
                            switch (displayMode)
                            {
                                case DisplayMode.Initial:
                                    //string id = sr.ReadLine();
                                    //Console.WriteLine("Connect to am350 - " + id);
                                    Thread.Sleep(10);
                                    displayMode = DisplayMode.Pile;
                                    modeChanged = true;
                                    break;
                                case DisplayMode.Pile:
                                    String msg = "1 pile1 2 pile2 3 pile3";
                                    if (pileIndex == 0)
                                    {
                                        msg = "1 pile1 2 pile2 3 pile3";  
                                    }
                                    else if (pileIndex == 1)
                                    {
                                        msg = "1 pile2 2 pile3 3 pile1";
                                    }
                                    else if (pileIndex == 2)
                                    {
                                        msg = "1 pile3 2 pile1 3 pile2";
                                    }
                                    sw.WriteLine(msg);
                                    //sw2.WriteLine(msg);
                                    //sw3.WriteLine(msg);
                                    modeChanged = false; //avoid sending command for many times
                                    break;
                                case DisplayMode.Stack:
                                    break;
                                case DisplayMode.Linear:
                                    break;
                                case DisplayMode.Fan:
                                    break;
                            }
                        }

                        Thread.Sleep(50);
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine(e);
                }
            }

        }

        void MainLoopToReadArduino(object sender, EventArgs e)
        {
            //if (readInit)
            //{
            //    input.ReadSideBend();
            //}
        }

        void KeyDownEventHandler(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Q)
                ;
                //sw.WriteLine("Quit");
            if (e.Key == Key.C)
            {
                pileIndex++;
                pileIndex %= 3;
                modeChanged = true;
            }
            if (e.Key == Key.M)
            {
                switch(displayMode)
                {
                    case DisplayMode.Pile:
                        displayMode = DisplayMode.Stack;
                        modeChanged = true;
                        break;
                    case DisplayMode.Stack:
                        displayMode = DisplayMode.Linear;
                        modeChanged = true;
                        break;
                    case DisplayMode.Linear:
                        displayMode = DisplayMode.Fan;
                        modeChanged = true;
                        break;
                    case DisplayMode.Fan:
                        displayMode = DisplayMode.Pile;
                        modeChanged = true;
                        break;

                }
            }

            //if (e.Key == Key.O && !readInit)
            //{
            //    //readThread = new Thread(InputThread);
            //    //readThread.Start();
            //    readInit = true;
            //}

            //if (e.Key == Key.C)
            //{
            //    //HttpGet();
            //    playSound(1);
            //}
            //if (e.Key == Key.V)
            //{
            //    playSound(4);
            //}
        }



    }
}
