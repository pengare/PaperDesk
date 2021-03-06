﻿using System;
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
        static string HostIP = "130.15.5.136";
        //static string HostIP = "192.168.0.105";
        enum DisplayMode
        {
            Initial,
            Pile,
            Stack,
            Linear,
            Fan
        }

        enum TaskType
        {
            Preliminary,
            Task1Doc,
            Task2Photo,
            Task3Email,
            Task4Map
        }
        TaskType taskType = TaskType.Task1Doc;

        DisplayMode displayMode = DisplayMode.Initial;
        Boolean modeChanged = true;

        string keyCode = "";
        Boolean newKeyPressed = false;

        //index used to indicate which images shown on device1
        int pileIndex = 0;

        // Timing
        DispatcherTimer mainTimer = new DispatcherTimer();
        int timerStep = 50;
        int readCounter = 0;

        //eInk Socket
        static TcpListener eink = new TcpListener(IPAddress.Parse(HostIP), 7778);
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
                        //if (modeChanged)
                        if(newKeyPressed)
                        {
                            newKeyPressed = false;

                            String msg = keyCode;
                            sw.WriteLine(msg);
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

            if (e.Key == Key.A)
                keyCode = "a";
            if (e.Key == Key.S)
                keyCode = "s";
            if (e.Key == Key.D)
                keyCode = "d";
            //if (e.Key == Key.W)
            //    keyCode = "w";
            if (e.Key == Key.E)
                keyCode = "e";
            if (e.Key == Key.R)
                keyCode = "r";
            if (e.Key == Key.J)
                keyCode = "show";
            if (e.Key == Key.K)
                keyCode = "hide";
            if (e.Key == Key.N)
                keyCode = "map,1*2";
            if (e.Key == Key.M)
                keyCode = "map,2*1";
            if (e.Key == Key.Left)
                keyCode = "left";
            if (e.Key == Key.Right)
                keyCode = "right";
            if (e.Key == Key.Up)
                keyCode = "up";
            if (e.Key == Key.Down)
                keyCode = "down";
            if (e.Key == Key.Enter)
                keyCode = "enter";
            
            if (e.Key == Key.H)
                keyCode = "hot";
            if (e.Key == Key.W)
                keyCode = "warm";
            if (e.Key == Key.C)
                keyCode = "cold";

            if (e.Key == Key.N)
                keyCode = "collocate#0:1";
            if (e.Key == Key.M)
                keyCode = "collocate#1:0";

            if (e.Key == Key.T) //simulate top bend sensor up
                keyCode = "key#1:bendsensortopup";
            if (e.Key == Key.Y)
                keyCode = "key#1:bendsensortopdown";
            if (e.Key == Key.U)
                keyCode = "key#1:bendsensorleftup";
            if (e.Key == Key.I)
                keyCode = "key#1:bendsensorleftdown";
            if (e.Key == Key.O)
                keyCode = "key#0:bendsensortopup";
            if (e.Key == Key.P)
                keyCode = "key#0:bendsensortopdown";
            if (e.Key == Key.K)
                keyCode = "key#0:bendsensorleftup";
            if (e.Key == Key.L)
                keyCode = "key#0:bendsensorleftdown";

            if (e.Key == Key.H)
                keyCode = "zone#0:hot";
            if (e.Key == Key.W)
                keyCode = "zone#0:warm";
            if (e.Key == Key.C)
                keyCode = "zone#0:cold";

            if (e.Key == Key.G)
                keyCode = "zone#1:hot";
            if (e.Key == Key.F)
                keyCode = "zone#1:warm";
            if (e.Key == Key.D)
                keyCode = "zone#1:cold";

            if (e.Key == Key.F2)
                keyCode = "zone#2:hot";
            if (e.Key == Key.F3)
                keyCode = "zone#2:warm";
            if (e.Key == Key.F4)
                keyCode = "zone#2:cold";
            
            //Training
            if (e.Key == Key.Z)
                keyCode = "tap#0:1:30";
            if (e.Key == Key.X)
                keyCode = "tap#0:1:reset";

            if (taskType == TaskType.Task1Doc)
            {
                //Task 1
                if (e.Key == Key.V)
                    keyCode = "tap#0:2:30:50";
                else if (e.Key == Key.B)
                    keyCode = "tap#1:0:200:250";
                else if (e.Key == Key.N)
                    keyCode = "tap#1:0:200:900";
                else if(e.Key == Key.M)
                    keyCode = "collocate#0:1";
            }
            else if(taskType == TaskType.Task2Photo)
            {
            //Task 2
            if (e.Key == Key.V)
                keyCode = "tap#0:2:30:50";
            if (e.Key == Key.B)
                keyCode = "tap#1:2:20:30";
            }
            else if (taskType == TaskType.Task3Email)
            {
                //Task 3
                if (e.Key == Key.V)
                    keyCode = "tap#0:2:30:40";
                if (e.Key == Key.B)
                    keyCode = "tap#1:0:30:40";

                else if (e.Key == Key.N)
                    keyCode = "tap#2:1:30:40";
                else if (e.Key == Key.M)
                    keyCode = "tap#2:0:30:40";

                else if (e.Key == Key.F1)
                    keyCode = "key#0:NewEmail";
                else if (e.Key == Key.Q)
                    keyCode = "key#keycode:Q";
                else if (e.Key == Key.E)
                    keyCode = "key#keycode:E";
                else if (e.Key == Key.Left)
                    keyCode = "key#keycode:LEFT";
                else if (e.Key == Key.Enter)
                    keyCode = "key#keycode:ENTER";
            }
            else if (taskType == TaskType.Task4Map)
            {
                //Task 4
                if (e.Key == Key.V)
                    keyCode = "tap#0:2:30:40";
                if (e.Key == Key.B)
                    keyCode = "collocate#0:1";
                if (e.Key == Key.Right)
                    keyCode = "yOffsetInc";
                if (e.Key == Key.Left)
                    keyCode = "yOffsetDec";
                if (e.Key == Key.Down)
                    keyCode = "xOffsetDec";
                if (e.Key == Key.Up)
                    keyCode = "xOffsetInc";
                else if (e.Key == Key.Q)
                    keyCode = "key#keycode:Q";
                else if (e.Key == Key.E)
                    keyCode = "key#keycode:E";
                else if (e.Key == Key.X)
                    keyCode = "tap#2:0";
            }



            if (e.Key == Key.Escape)
                keyCode = "taskChooser";

            newKeyPressed = true;

            if (e.Key == Key.D0)
            {
                taskType = TaskType.Preliminary;
                newKeyPressed = false;
            }
            else if (e.Key == Key.D1)
            {
                taskType = TaskType.Task1Doc;
                newKeyPressed = false;
            }
            else if (e.Key == Key.D2)
            {
                taskType = TaskType.Task2Photo;
                newKeyPressed = false;
            }
            else if (e.Key == Key.D3)
            {
                taskType = TaskType.Task3Email;
                newKeyPressed = false;
            }
            else if (e.Key == Key.D4)
            {
                taskType = TaskType.Task4Map;
                newKeyPressed = false;
            }
                

        }



    }
}
