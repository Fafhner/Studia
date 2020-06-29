﻿// <auto-generated />
using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;
using PizzeriaProject.Data;

namespace PizzeriaProject.Migrations
{
    [DbContext(typeof(PizzasDbContext))]
    [Migration("20200609194959_Update_v01")]
    partial class Update_v01
    {
        protected override void BuildTargetModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("ProductVersion", "3.1.4")
                .HasAnnotation("Relational:MaxIdentifierLength", 128)
                .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

            modelBuilder.Entity("PizzeriaProject.Models.Datas.PizzaDoughData", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<decimal>("Price")
                        .HasColumnType("money");

                    b.Property<string>("Type")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.HasKey("Id");

                    b.ToTable("Doughs");

                    b.HasData(
                        new
                        {
                            Id = 1,
                            Price = 3.0m,
                            Type = "Thin-crust"
                        },
                        new
                        {
                            Id = 2,
                            Price = 4.0m,
                            Type = "Thick-crust"
                        });
                });

            modelBuilder.Entity("PizzeriaProject.Models.Datas.PizzaIngredientData", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("Name")
                        .IsRequired()
                        .HasColumnType("nvarchar(25)")
                        .HasMaxLength(25);

                    b.Property<decimal>("Price")
                        .HasColumnType("money");

                    b.HasKey("Id");

                    b.ToTable("Ingriedients");

                    b.HasData(
                        new
                        {
                            Id = 1,
                            Name = "Cheese",
                            Price = 2.0m
                        },
                        new
                        {
                            Id = 2,
                            Name = "Ham",
                            Price = 5.0m
                        },
                        new
                        {
                            Id = 3,
                            Name = "Chicken",
                            Price = 5.0m
                        },
                        new
                        {
                            Id = 4,
                            Name = "Corn",
                            Price = 3.0m
                        },
                        new
                        {
                            Id = 5,
                            Name = "Sausage",
                            Price = 6.0m
                        },
                        new
                        {
                            Id = 6,
                            Name = "Tomato sauce",
                            Price = 1.0m
                        },
                        new
                        {
                            Id = 7,
                            Name = "Green pepper",
                            Price = 2.0m
                        },
                        new
                        {
                            Id = 8,
                            Name = "Red pepper",
                            Price = 2.0m
                        },
                        new
                        {
                            Id = 9,
                            Name = "Onion",
                            Price = 2.0m
                        },
                        new
                        {
                            Id = 10,
                            Name = "Mushrooms",
                            Price = 2.0m
                        });
                });

            modelBuilder.Entity("PizzeriaProject.Models.Datas.PizzaSizeData", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<decimal>("Price")
                        .HasColumnType("money");

                    b.Property<string>("Size")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.HasKey("Id");

                    b.ToTable("Sizes");

                    b.HasData(
                        new
                        {
                            Id = 1,
                            Price = 3.0m,
                            Size = "Small"
                        },
                        new
                        {
                            Id = 2,
                            Price = 5.0m,
                            Size = "Medium"
                        },
                        new
                        {
                            Id = 3,
                            Price = 7.0m,
                            Size = "Big"
                        });
                });

            modelBuilder.Entity("PizzeriaProject.Models.Items.PizzaIngredientItem", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<int>("IngredientId")
                        .HasColumnType("int");

                    b.Property<int>("PizzaId")
                        .HasColumnType("int");

                    b.Property<int>("Quantity")
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.HasIndex("IngredientId");

                    b.HasIndex("PizzaId");

                    b.ToTable("PizzaIngriedients");

                    b.HasData(
                        new
                        {
                            Id = 1,
                            IngredientId = 1,
                            PizzaId = 1,
                            Quantity = 3
                        },
                        new
                        {
                            Id = 2,
                            IngredientId = 2,
                            PizzaId = 1,
                            Quantity = 1
                        },
                        new
                        {
                            Id = 3,
                            IngredientId = 3,
                            PizzaId = 1,
                            Quantity = 1
                        });
                });

            modelBuilder.Entity("PizzeriaProject.Models.Items.PizzaItem", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<int>("DoughId")
                        .HasColumnType("int");

                    b.Property<string>("Name")
                        .IsRequired()
                        .HasColumnType("nvarchar(25)")
                        .HasMaxLength(25);

                    b.Property<int>("SizeId")
                        .HasColumnType("int");

                    b.Property<DateTime>("Timestamp")
                        .HasColumnType("datetime2");

                    b.HasKey("Id");

                    b.HasIndex("DoughId");

                    b.HasIndex("SizeId");

                    b.ToTable("Pizzas");

                    b.HasData(
                        new
                        {
                            Id = 1,
                            DoughId = 2,
                            Name = "SuperPizza",
                            SizeId = 3,
                            Timestamp = new DateTime(2020, 6, 9, 21, 49, 57, 946, DateTimeKind.Local).AddTicks(7626)
                        });
                });

            modelBuilder.Entity("PizzeriaProject.Models.Items.PizzaIngredientItem", b =>
                {
                    b.HasOne("PizzeriaProject.Models.Datas.PizzaIngredientData", "Ingredient")
                        .WithMany()
                        .HasForeignKey("IngredientId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("PizzeriaProject.Models.Items.PizzaItem", "Pizza")
                        .WithMany("Ingredients")
                        .HasForeignKey("PizzaId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();
                });

            modelBuilder.Entity("PizzeriaProject.Models.Items.PizzaItem", b =>
                {
                    b.HasOne("PizzeriaProject.Models.Datas.PizzaDoughData", "Dough")
                        .WithMany()
                        .HasForeignKey("DoughId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("PizzeriaProject.Models.Datas.PizzaSizeData", "Size")
                        .WithMany()
                        .HasForeignKey("SizeId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();
                });
#pragma warning restore 612, 618
        }
    }
}
