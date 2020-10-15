using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace PizzeriaProject.Migrations
{
    public partial class InitCreate : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "Doughs",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Type = table.Column<string>(nullable: false),
                    Price = table.Column<decimal>(type: "money", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Doughs", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Ingriedients",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Name = table.Column<string>(maxLength: 25, nullable: false),
                    Price = table.Column<decimal>(type: "money", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Ingriedients", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Sizes",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Size = table.Column<string>(nullable: false),
                    Price = table.Column<decimal>(type: "money", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Sizes", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Pizzas",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Name = table.Column<string>(maxLength: 25, nullable: false),
                    SizeId = table.Column<int>(nullable: false),
                    DoughId = table.Column<int>(nullable: false),
                    Timestamp = table.Column<DateTime>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Pizzas", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Pizzas_Doughs_DoughId",
                        column: x => x.DoughId,
                        principalTable: "Doughs",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Pizzas_Sizes_SizeId",
                        column: x => x.SizeId,
                        principalTable: "Sizes",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "PizzaIngriedients",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Quantity = table.Column<int>(nullable: false),
                    IngredientId = table.Column<int>(nullable: false),
                    PizzaId = table.Column<int>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_PizzaIngriedients", x => x.Id);
                    table.ForeignKey(
                        name: "FK_PizzaIngriedients_Ingriedients_IngredientId",
                        column: x => x.IngredientId,
                        principalTable: "Ingriedients",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_PizzaIngriedients_Pizzas_PizzaId",
                        column: x => x.PizzaId,
                        principalTable: "Pizzas",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.InsertData(
                table: "Doughs",
                columns: new[] { "Id", "Price", "Type" },
                values: new object[,]
                {
                    { 1, 3.0m, "Thin-crust" },
                    { 2, 4.0m, "Thick-crust" }
                });

            migrationBuilder.InsertData(
                table: "Ingriedients",
                columns: new[] { "Id", "Name", "Price" },
                values: new object[,]
                {
                    { 1, "Cheese", 3.0m },
                    { 2, "Ham", 5.0m },
                    { 3, "Chcicken", 5.0m },
                    { 4, "Corn", 3.0m }
                });

            migrationBuilder.InsertData(
                table: "Sizes",
                columns: new[] { "Id", "Price", "Size" },
                values: new object[,]
                {
                    { 1, 3.0m, "Small" },
                    { 2, 5.0m, "Medium" },
                    { 3, 7.0m, "Big" }
                });

            migrationBuilder.InsertData(
                table: "Pizzas",
                columns: new[] { "Id", "DoughId", "Name", "SizeId", "Timestamp" },
                values: new object[] { 1, 2, "SuperPizza", 3, new DateTime(2020, 6, 9, 16, 18, 46, 708, DateTimeKind.Local).AddTicks(3177) });

            migrationBuilder.InsertData(
                table: "PizzaIngriedients",
                columns: new[] { "Id", "IngredientId", "PizzaId", "Quantity" },
                values: new object[] { 1, 1, 1, 3 });

            migrationBuilder.InsertData(
                table: "PizzaIngriedients",
                columns: new[] { "Id", "IngredientId", "PizzaId", "Quantity" },
                values: new object[] { 2, 2, 1, 1 });

            migrationBuilder.InsertData(
                table: "PizzaIngriedients",
                columns: new[] { "Id", "IngredientId", "PizzaId", "Quantity" },
                values: new object[] { 3, 3, 1, 1 });

            migrationBuilder.CreateIndex(
                name: "IX_PizzaIngriedients_IngredientId",
                table: "PizzaIngriedients",
                column: "IngredientId");

            migrationBuilder.CreateIndex(
                name: "IX_PizzaIngriedients_PizzaId",
                table: "PizzaIngriedients",
                column: "PizzaId");

            migrationBuilder.CreateIndex(
                name: "IX_Pizzas_DoughId",
                table: "Pizzas",
                column: "DoughId");

            migrationBuilder.CreateIndex(
                name: "IX_Pizzas_SizeId",
                table: "Pizzas",
                column: "SizeId");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "PizzaIngriedients");

            migrationBuilder.DropTable(
                name: "Ingriedients");

            migrationBuilder.DropTable(
                name: "Pizzas");

            migrationBuilder.DropTable(
                name: "Doughs");

            migrationBuilder.DropTable(
                name: "Sizes");
        }
    }
}
